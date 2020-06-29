package com.pomo.miaosha.domain;

import java.util.Date;
/*
* 秒杀商品
* */
public class MiaoshaGoods {
	private Long id; //主键ID
	private Long goodsId; //商品ID
	private Integer stockCount; //库存
	private Date startDate;  //开始秒杀的时间
	private Date endDate;  //结束秒杀的时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
