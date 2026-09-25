package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 历史订单
     * */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 取消订单
     * */
    @Update("update orders set status = 6 where id = #{id}")
    void cancel(Long id);

    /**
     * 订单条件搜索 conditionSearch
     * */
    Page<Orders> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态太查询字段
     * */
    @Select("select count(*) from orders where status = #{toBeConfirmed}")
    Integer countByStatus(Integer toBeConfirmed);

    /**
     * 接单 confirm
     * 根据id来进行接单
     * */
    @Update("update orders set status = 3 where id = #{id};")
    void confirm(Long id);

    /**
     * 查询
     * */
    @Select("select sum(amount) from orders where status = 5 and checkout_time > #{begin} and checkout_time < #{end}")
    Double sum(HashMap<Object, Object> objectObjectHashMap);

    /**
     * 更新这三个数据
     * */
    @Update("update orders set status = #{status} ,delivery_time = #{deliveryTime} where id = #{id}")
    void completeUpdate(Integer status, LocalDateTime deliveryTime, Long id);

    Integer countValidOrder(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    /**
     * 根据当前数据传递当前菜品的销量和名称
     * */
    List<GoodsSalesDTO> top10(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 根据动态条件统计订单数量（begin/end 时间段 + status 状态）
     * */
    Integer countByMap(Map map);

    /**
     * 根据动态条件统计营业额（begin/end 时间段 + status 状态）
     * */
    Double sumByMap(Map map);
}
