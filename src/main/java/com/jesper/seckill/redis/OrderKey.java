package com.jesper.seckill.redis;

/**
 * Created by jiangyunxiong on 2018/5/29.
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static OrderKey getSeckillOrderByUidGid = new OrderKey(0, "seckill");
    public static OrderKey getOrderById = new OrderKey(0, "order");
}
