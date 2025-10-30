package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 自定义定时任务,实现订单状态定时处理
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理支付超时的订单(每分钟处理一次)
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void processTimeoutOrder() {
        log.info("处理支付超时的订单:{}", new Date());

        LocalDateTime time = LocalDateTime.now().minusMinutes(-15);
        // 查询状态为待支付，且下单时间超过15分钟的订单
        List<Orders> orderList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);

        if (orderList != null && orderList.size() > 0) {
            for (Orders order : orderList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("支付超时,自动取消订单");
                order.setCancelTime(LocalDateTime.now());

                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理"派送中"状态的订单(凌晨一点统一处理)
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("处理派送中的订单:{}", new Date());

        LocalDateTime time = LocalDateTime.now().minusMinutes(-60);
        //获取状态为"派送中"，下单时间超过1小时的订单
        List<Orders> orderList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);

        if (orderList != null && orderList.size() > 0) {
            for (Orders order : orderList) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }


}
