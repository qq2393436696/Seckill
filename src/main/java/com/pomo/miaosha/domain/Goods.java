package com.pomo.miaosha.domain;

/*
 * 商品
 * */
public class Goods {
	private Long id;  //ID
	private String goodsName;    //商品名称
	private String goodsTitle;  //商品标题
	private String goodsImg;    //商品图片
	private String goodsDetail;  //商品详细
	private Double goodsPrice;   //商品价格
	private Integer goodsStock;  //商品库存

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getGoodsStock() {
		return goodsStock;
	}

	public void setGoodsStock(Integer goodsStock) {
		this.goodsStock = goodsStock;
	}
}