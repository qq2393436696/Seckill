package com.pomo.miaosha.service;

import com.pomo.miaosha.dao.GoodsDao;
import com.pomo.miaosha.domain.Goods;
import com.pomo.miaosha.domain.MiaoshaGoods;
import com.pomo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsService {

	@Autowired
	public GoodsDao goodsDao;

	public List<GoodsVo> listGoodsVo() {
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	/**
	 * 减商品库存
	 * @param  goodsVo 商品
	 * @return boolean 返回减库存是否成功
	 */
	public boolean reduceStock(GoodsVo goodsVo) {
		MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
		miaoshaGoods.setGoodsId(goodsVo.getId());
		int ret = goodsDao.reduceStock(miaoshaGoods);
		return ret > 0;
	}

	/**
	 * 库存复位
	 * @param goodsList 商品列表
	 * */
	public void resetStock(List<GoodsVo> goodsList) {
		goodsList.forEach(goods -> {
			MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
			miaoshaGoods.setGoodsId(goods.getId());
			miaoshaGoods.setStockCount(goods.getStockCount());
			goodsDao.resetStock(miaoshaGoods);
		});

	}
}
