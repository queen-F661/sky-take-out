package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 新增订单
     * */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * 模拟支付时需要先根据前端传来的订单号，把这条订单从数据库里查出来
     * */
    @Select("select * from orders where number = #{number}")
    Orders getByNumber(String number);

    /**
     * 根据主键动态更新订单字段
     * paySuccess 里只改 payStatus、status、checkoutTime 这 3 个字段，
     * 所以这里用 <if> 动态 SQL：传了哪个字段就更新哪个字段，没传的不动
     * */
    void update(Orders orders);

    /**
     * 要查询当前有没有订单超时为15分钟且订单处于是在待付款方式
     * */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime};")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * 根据id来查询订单
     * */
    @Select("select * from orders where id = #{id};")
    Orders getById(Long id);
}
