//package com.sky.task;
//
//import com.sky.entity.Orders;
//import com.sky.mapper.OrderMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@Slf4j
//public class OrderTask {
//    @Autowired
//    private OrderMapper orderMapper;
//
//    /**
//     * 处理超时订单的方法
//     * 每分钟处理一次
//     * */
//    @Scheduled(cron = "1/5 * * * * *")
//    // @Scheduled(cron = "0 * * * * ?")
//    public void processTimeoutOrder(){
//        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
//        // 1.要查询当前有没有订单超时为15分钟且订单处于是在待付款方式
//        List<Orders> byStatusAndOrderTime = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, localDateTime);
//        // 2.根据这个查询
//        // 用查询的结果进行修改字段
//        // 一个是修改成已取消
//        // 一个把原因固定死
//        // 还有一个是把当前的
//        // 在把订单取消时间加上
//        if(byStatusAndOrderTime != null && byStatusAndOrderTime.size() > 0){
//            for (Orders orders : byStatusAndOrderTime) {
//                orders.setStatus(Orders.CANCELLED);
//                orders.setRemark("用户超时,自动取消");
//                orders.setCancelTime(LocalDateTime.now());
//                orderMapper.update(orders);
//            }
//        }
//    }
//
//    /**
//     * 每天凌晨1点来进行查询订单是否处于派送中
//     * 处理派送中的订单
//     * */
//    @Scheduled(cron = "0/5 * * * * *")
//    // @Scheduled(cron = "0 0 1 * * ?")
//    public void processDeliveryOrder(){
//        // 第一步 还是先查询当前的值 但是这次有点不一样
//        // status 是处于派送中
//        // 时间可以减去一个小时 就能知道上个月还在派送中的订单
//        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);
//        List<Orders> byStatusAndOrderTime = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, localDateTime);
//
//        if(byStatusAndOrderTime != null && byStatusAndOrderTime.size() > 0){
//            for (Orders orders : byStatusAndOrderTime) {
//                orders.setStatus(Orders.COMPLETED);
//
//                orderMapper.update(orders);
//            }
//        }
//
//    }
//}
