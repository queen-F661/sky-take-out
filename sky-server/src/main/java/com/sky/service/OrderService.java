package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 用户下单
     * */
    OrderSubmitVO sumbit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 模拟支付成功
     * 真实场景：微信支付成功后，微信服务器会回调我们的 paySuccess 接口
     * 模拟场景：没有商户资质，用户点支付时后端直接把订单置为已支付
     * @param orderNumber 订单号
     * */
    void paySuccess(String orderNumber);

    /**
     * 用户催单
     * */
    void reminder(Long id);
}
