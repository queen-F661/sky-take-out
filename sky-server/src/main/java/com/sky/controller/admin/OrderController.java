package com.sky.controller.admin;

import com.sky.dto.AddressBookDTO;
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
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;


/**
 * 用户订单接口
 */
@Slf4j
@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单条件搜索 conditionSearch
     * */
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){

        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 各个状态的订单数量统计
     * */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics(){

        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }

    /**
     * 商家订单详情（复用 details）
     * */
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable Long id){

        OrderVO orderVO = orderService.detail(id);

        return Result.success(orderVO);
    }

    /**
     * 接单 confirm
     * 根据id来进行接单
     * */
    @PutMapping("/confirm")
    public Result<Object> confirm(@RequestBody AddressBookDTO addressBookDTO){

        // 这个必须是1才能进行接单
        // 要进行判断当前 如果为1

        orderService.confirm(addressBookDTO.getId());

        return Result.success();
    }

    /**
     * 拒单 rejection
     * */
    @PutMapping("/rejection")
    public Result<Object> rejection(@RequestBody AddressBookDTO addressBookDTO){

        orderService.rejection(addressBookDTO);

        return Result.success();
    }

    /**
     * 商家取消订单 cancel
     * */
//    public Result<Object> cancel(){
//
//    }
}
