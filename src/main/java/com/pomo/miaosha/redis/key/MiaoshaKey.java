package com.pomo.miaosha.redis.key;

import com.pomo.miaosha.redis.BasePrefix;

public class MiaoshaKey extends BasePrefix {

    private MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0, "GoodsOver");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "MiaoshaPath");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "MiaoshaVerifyCode");
}
