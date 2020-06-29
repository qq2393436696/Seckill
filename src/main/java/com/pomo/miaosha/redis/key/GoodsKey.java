package com.pomo.miaosha.redis.key;

import com.pomo.miaosha.redis.BasePrefix;

public class GoodsKey extends BasePrefix {

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static GoodsKey getGoodsList = new GoodsKey(60, "goodlist");
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gooddetail");
	public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "goodsstock");
}
