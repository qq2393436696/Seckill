package com.pomo.miaosha.vo;

import com.pomo.miaosha.domain.MiaoshaUser;

/*
 * 商品详情
 * */
public class GoodsDetailVo {

	private int miaoshaStatus = 0;  //秒杀状态
	private int remainSeconds = 0;    //
	private GoodsVo goods;    //商品
	private MiaoshaUser user;    //用户

	public int getMiaoshaStatus() {
		return miaoshaStatus;
	}

	public void setMiaoshaStatus(int miaoshaStatus) {
		this.miaoshaStatus = miaoshaStatus;
	}

	public int getRemainSeconds() {
		return remainSeconds;
	}

	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}

	public GoodsVo getGoods() {
		return goods;
	}

	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}

	public MiaoshaUser getUser() {
		return user;
	}

	public void setUser(MiaoshaUser user) {
		this.user = user;
	}
}
