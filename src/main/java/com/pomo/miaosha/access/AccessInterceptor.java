package com.pomo.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.pomo.miaosha.domain.MiaoshaUser;
import com.pomo.miaosha.redis.key.AccessKey;
import com.pomo.miaosha.redis.RedisService;
import com.pomo.miaosha.result.CodeMsg;
import com.pomo.miaosha.result.Result;
import com.pomo.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/*
* 存取拦截器  （继承与处理器适配器、拦截器）
* */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	MiaoshaUserService userService;

	@Autowired
	RedisService redisService;

	/*
	 * 预先处理
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			MiaoshaUser user = getUser(request, response);
			UserContext.setUser(user);

			HandlerMethod hm = (HandlerMethod) handler;
			//获取方法上的注解
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if (needLogin) {
				if (user == null) {
					render(response, CodeMsg.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getId();
			} else {
				//do nothing
			}

			AccessKey ak = AccessKey.withExpire(seconds);
			Integer count = redisService.get(ak, key, Integer.class);
			if (count == null) {
				redisService.set(ak, key, 1);
			} else if (count < 5) {
				redisService.incr(ak, key);
			} else {
				render(response, CodeMsg.ACCESS_LIMIT_REACHED);
				return false;
			}
		}

		return true;
	}

	/*
	 * 渲染
	 * */
	private void render(HttpServletResponse response, CodeMsg codeMsg) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str = JSON.toJSONString(Result.error(codeMsg));
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	/*
	 * 获得用户
	 * */
	private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		return userService.getByToken(response, token);
	}

	/*
	 * 获得cookie的值
	 * */
	private String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
