package com.pomo.miaosha.domain;
/*
* 秒杀订单
* */
public class MiaoshaOrder {
    private Long id; //ID
    private Long userId;  //用户ID
    private Long  orderId;  //订单ID
    private Long goodsId;  //商品ID
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
