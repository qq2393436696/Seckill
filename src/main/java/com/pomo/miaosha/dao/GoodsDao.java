package com.pomo.miaosha.dao;

import com.pomo.miaosha.domain.MiaoshaGoods;
import com.pomo.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

	/*
	 *查询所有商品集合
	 * */
	@Select("select goods.*, miaoshaGoods.stock_count, miaoshaGoods.start_date, miaoshaGoods.end_date, miaoshaGoods.miaosha_price "
			+ "from miaosha_goods miaoshaGoods left join goods goods on miaoshaGoods.goods_id = goods.id")
	public List<GoodsVo> listGoodsVo();

	/*
	 * 根据goodsId查询商品
	 * */
	@Select("select goods.*, miaoshaGoods.stock_count, miaoshaGoods.start_date, miaoshaGoods.end_date, miaoshaGoods.miaosha_price "
			+ "from miaosha_goods miaoshaGoods left join goods goods on miaoshaGoods.goods_id = goods.id where goods.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	/**
	 * 减商品库存
	 * @param  miaoshaGoods 秒杀商品
	 * @return int 返回修改成功的个数
	 */
	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	public int reduceStock(MiaoshaGoods miaoshaGoods);

	/**
	 * 库存复位
	 * @param  miaoshaGoods 秒杀商品
	 * @return int 返回修改成功的个数
	 */
	@Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
	public int resetStock(MiaoshaGoods miaoshaGoods);
}
