package com.pomo.miaosha.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 验证工具类
 * */
public class ValidatorUtil {
	//1开头，然后10个数字，那么正确的手机号
	//验证手机号格式
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");


	/*
	 * 验证是不是手机号
	 * */
	public static boolean isMobile(String src) {
		if (StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);

		return m.matches();//boolean matches() 尝试对整个目标字符展开匹配检测,也就是只有整个目标字符串完全匹配时才返回真值.
	}

	public static void main(String[] args) {
		System.out.println(isMobile("12345678903"));
		System.out.println(isMobile("123456"));
	}
}
