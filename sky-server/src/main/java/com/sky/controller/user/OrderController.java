package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用户订单接口
 */
@Slf4j
@RestController
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){

        // 把当前的数据传递给service端
        OrderSubmitVO orderSubmitVO = orderService.sumbit(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    /**
     * 模拟支付
     * 前端(uniapp小程序)点"去支付"时调的就是这个接口：PUT /user/order/payment
     * 前端 request 封装里 Content-Type 是 application/json，
     * 参数以 JSON 请求体方式传来，所以这里必须用 @RequestBody 接，否则接到的全是 null
     *
     * 原课程流程：这里应该调 WeChatPayUtil 去微信统一下单，把支付参数返回给前端拉起收银台
     * 模拟流程：跳过微信，直接调 paySuccess 把订单置为已支付，返回一个空 VO 撑住返回结构
     * */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        // 模拟支付成功：后端直接把订单改成"已支付/待接单"
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        // OrderPaymentVO 里的签名、时间戳等字段是给微信收银台用的，现在用不上了，
        // 返回空对象只是为了不破坏前端的返回格式(res.code === 1 && res.data)
        return Result.success(new OrderPaymentVO());
    }

    /**
     * 用户催单
     * */
    @GetMapping("/reminder/{id}")
    public Result<Object> reminder(@PathVariable Long id){

        log.info("客户催单");

        orderService.reminder(id);

        return Result.success();
    }

    /**
     * 历史订单查询
     * */
    @GetMapping("/historyOrders")
    public Result<PageResult> history(OrdersPageQueryDTO ordersPageQueryDTO){

        PageResult pageResults = orderService.history(ordersPageQueryDTO);

        return Result.success(pageResults);
    }
}
