package com.pomo.miaosha.redis.key;

import com.pomo.miaosha.redis.BasePrefix;

public class UserKey extends BasePrefix {

	private UserKey(String prefix) {
		super(prefix);
	}

	public static UserKey getById = new UserKey("id");
	public static UserKey getByName = new UserKey("name");
}
