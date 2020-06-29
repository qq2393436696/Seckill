package com.pomo.miaosha.access;

import com.pomo.miaosha.domain.MiaoshaUser;
/*
* 用户报文
* */
public class UserContext {

	private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

	/*
	* 创建user
	* */
	public static void setUser(MiaoshaUser user) {
		userHolder.set(user);
	}

	/*
	* 获取user
	* */
	public static MiaoshaUser getUser() {
		return userHolder.get();
	}

}
