package com.pomo.miaosha.vo;

import com.pomo.miaosha.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
/*
 * 登录
 * */

public class LoginVo {
	/*
	 * 手机号
	 * 密码
	 * */
	@NotNull
	@IsMobile
	private String mobile;

	@NotNull
	@Length(min = 32)
	private String password;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginVo{" + "mobile='" + mobile + '\'' + ", password='" + password + '\'' + '}';
	}
}
