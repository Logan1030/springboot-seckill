package com.jesper.seckill.job;

import com.jesper.seckill.service.OrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 秒杀定时任务
 */
@Component
public class SeckillJobHandler {
    private static Logger logger = LoggerFactory.getLogger(SeckillJobHandler.class);

    @Autowired
    private OrderService orderService;

    /**
     * 清理过期订单
     */
    @XxlJob("cleanExpiredOrders")
    public void cleanExpiredOrders() {
        try {
            XxlJobHelper.log("开始清理过期订单...");
            int count = orderService.cleanExpiredOrders();
            XxlJobHelper.log("清理完成，共清理{}个过期订单", count);
        } catch (Exception e) {
            logger.error("清理过期订单失败", e);
            XxlJobHelper.log("清理过期订单失败: {}", e.getMessage());
        }
    }
} 