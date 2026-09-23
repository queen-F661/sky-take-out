package com.sky.controller.admin;

import com.sky.dto.AddressBookDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
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

import java.time.LocalDateTime;


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
    public Result<Object> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){

        orderService.rejection(ordersRejectionDTO);

        return Result.success();
    }

    /**
     * 商家取消订单 cancel
     * */
    @PutMapping("/cancel")
    public Result<Object> cancel(@RequestBody AddressBookDTO addressBookDTO){

        orderService.admincancel(addressBookDTO);

        return Result.success();
    }

    /**
     * 派送
     * */
    @PutMapping("/delivery/{id}")
    public Result<Object> delivery(@PathVariable Long id){

        orderService.delivery(id);

        return Result.success();
    }

    /**
     * 完成（送达）
     * */
    @PutMapping("/complete/{id}")
    public Result<Object> complete(@PathVariable Long id){

        // 要使用根据id来进行数据的修改
        orderService.complete(id);

        return Result.success();
    }
}
