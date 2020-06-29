package com.pomo.miaosha.dao;

import com.pomo.miaosha.domain.MiaoshaOrder;
import com.pomo.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

	/*
	 * 根据用户ID和商品ID获取秒杀订单
	 * */
	@Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	/*
	 * 插入订单
	 * */
	@Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
			+ "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
	@SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
	public long insert(OrderInfo orderInfo);

	/*
	 * 插入秒杀订单
	 * */
	@Insert("insert into miaosha_order(user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
	public void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

	/*
	 * 根据ID查询订单
	 * */
	@Select("select * from order_info where id = #{orderId}")
	public OrderInfo getOrderById(@Param("orderId") long orderId);

	/*
	 * 删除所有订单
	 * */
	@Delete("delete from order_info")
	public void deleteOrders();

	/*
	 * 删除秒杀订单
	 * */
	@Delete("delete from miaosha_order")
	public void deleteMiaoshaOrders();
}
