package com.pomo.miaosha.controller;

import com.pomo.miaosha.redis.RedisService;
import com.pomo.miaosha.result.Result;
import com.pomo.miaosha.service.MiaoshaUserService;
import com.pomo.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
/*
 * 1、跳转登录页面
 * 2、登录
 * */ public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);  //日志

	@Autowired
	MiaoshaUserService userService;

	@Autowired
	RedisService redisService;

	@RequestMapping("/to_login")   //跳转登录页面
	public String toLogin() {
		return "login";
	}


	//loginVo从前端传过来的手机号，密码（密码在前端被加密）
	@RequestMapping("/do_login")    //登录
	@ResponseBody
	public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
		log.info(loginVo.toString());

		//登录
		String token = userService.login(response, loginVo);
		System.out.println(loginVo.getMobile());
		System.out.println(loginVo.getPassword());
		return Result.success(token);
	}
}
