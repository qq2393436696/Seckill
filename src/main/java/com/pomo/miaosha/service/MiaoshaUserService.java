package com.pomo.miaosha.service;

import com.pomo.miaosha.dao.MiaoshaUserDao;
import com.pomo.miaosha.domain.MiaoshaUser;
import com.pomo.miaosha.exception.GlobalException;
import com.pomo.miaosha.redis.MiaoshaUserKey;
import com.pomo.miaosha.redis.RedisService;
import com.pomo.miaosha.result.CodeMsg;
import com.pomo.miaosha.util.MD5Util;
import com.pomo.miaosha.util.UUIDUtil;
import com.pomo.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    /*
    * 1、取缓存
    * 2、若缓存为空，取数据库
    * 3、建立缓存
    * */
    public MiaoshaUser getById(long id) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    /*
    * 1、取user
    * 2、更新数据库
    * 3、删除已有的缓存
    * 4、创建新的缓存
    * */
    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    /**
     * 通过token获取user，并在每次获取时延长token时间
     */
    /*
    * 1、判断token是否为空
    * 2、根据token查缓存
    * 3、延长token的有效期，延长两天
    * 4、返回user对象
    *
    * */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        //查缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);  //延长两天
        }
        return user;
    }

    /**
     * 登录逻辑
     * 1、先判断前端传过来的loginVo是否为空
     * 2、根据手机号码查询，判断手机号码是否存在，并且返回一个user对象，里面包含盐值和密码
     * 3、将前端传过来的密码进行盐值加密，然后跟数据库的密码对比
     * 4、添加token到cookie中
     */
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        //Long.parseLong（） 将一个字符串转换成数字的
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    /**
     * 添加token到cookie中
     */
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);//建立缓存
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);//实例化token
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());//加两天会话时长
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
