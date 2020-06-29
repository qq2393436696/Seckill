package com.pomo.miaosha.vo;

import com.pomo.miaosha.domain.OrderInfo;

/*
 * 订单详情
 * */
public class OrderDetailVo {

	private GoodsVo goods;        //商品
	private OrderInfo order;    //订单

	public GoodsVo getGoods() {
		return goods;
	}

	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}
}
