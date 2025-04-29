package com.jesper.job;

import com.jesper.seckill.bean.OrderInfo;
import com.jesper.seckill.redis.GoodsKey;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.service.GoodsService;
import com.jesper.seckill.service.OrderService;
import com.jesper.seckill.vo.GoodsVo;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SeckillJobHandler {
    private static Logger logger = LoggerFactory.getLogger(SeckillJobHandler.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    /**
     * 秒杀商品库存检查任务
     */
    @XxlJob("seckillStockCheckJobHandler")
    public void seckillStockCheck() {
        try {
            logger.info("开始执行秒杀商品库存检查任务");
            // 1. 获取所有秒杀商品
            List<GoodsVo> goodsList = goodsService.listGoodsVo();
            
            // 2. 检查每个商品的库存
            for (GoodsVo goods : goodsList) {
                // 获取Redis中的库存
                Long stock = redisService.get(GoodsKey.getGoodsStock, "" + goods.getId(), Long.class);
                if (stock != null && stock < 10) { // 库存低于10时发出警告
                    logger.warn("商品[{}]库存不足，当前库存：{}", goods.getGoodsName(), stock);
                    // TODO: 可以在这里添加发送通知的逻辑，比如发送邮件或短信
                }
            }
            
            XxlJobHelper.handleSuccess("秒杀商品库存检查任务执行成功");
        } catch (Exception e) {
            logger.error("秒杀商品库存检查任务执行失败", e);
            XxlJobHelper.handleFail("秒杀商品库存检查任务执行失败");
        }
    }

    /**
     * 秒杀订单状态检查任务
     */
    @XxlJob("seckillOrderCheckJobHandler")
    public void seckillOrderCheck() {
        try {
            logger.info("开始执行秒杀订单状态检查任务");
            
            // 1. 获取所有未支付的订单
            List<OrderInfo> unpaidOrders = orderService.getUnpaidOrders();
            
            // 2. 检查每个订单的创建时间
            Date now = new Date();
            for (OrderInfo order : unpaidOrders) {
                // 如果订单创建时间超过30分钟，则取消订单
                if (now.getTime() - order.getCreateDate().getTime() > 30 * 60 * 1000) {
                    logger.info("订单[{}]超时未支付，准备取消", order.getId());
                    // TODO: 实现取消订单的逻辑
                    // 1. 恢复商品库存
                    // 2. 更新订单状态
                    // 3. 发送通知给用户
                }
            }
            
            XxlJobHelper.handleSuccess("秒杀订单状态检查任务执行成功");
        } catch (Exception e) {
            logger.error("秒杀订单状态检查任务执行失败", e);
            XxlJobHelper.handleFail("秒杀订单状态检查任务执行失败");
        }
    }
} 