package com.pomo.miaosha.redis.key;

import com.pomo.miaosha.redis.BasePrefix;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}

	public static OrderKey getMiaoshaOrderByUisGid = new OrderKey("MiaoshaOrderByUisGid");
}
