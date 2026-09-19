package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 用户下单
     * */
    @Transactional
    @Override
    public OrderSubmitVO sumbit(OrdersSubmitDTO ordersSubmitDTO) {

        // 在这个之前 你肯定要进行判断异常情况
        // 比如说:
        // 你购物车为空 就不能设置订单
        // 因为要查看当前的用户下面的购物车有没有值 那肯定要把当前的用户id来进行放入进去
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        if(shoppingCarts == null){
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // 你地址表为空 也不能提交订单
        AddressBook addressBook = addressBookMapper.listById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 用户下单分为这几步
        // 先往订单表插入一行数据
        // 首先 你插入的肯定是一张实体类往我的mysql插入
        // 所以要把当前传递过来的数据放到实体类中了 放完成之后,那么把当前没有的数据传递给补全
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        // 把当前没有传递过来的字段传递过来
        orders.setUserId(userId);
        orders.setNumber(System.currentTimeMillis() + "");
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        // 下单时间就是你现在的系统时间
        orders.setOrderTime(LocalDateTime.now());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        // 在把数据传递给Mapper
        orderMapper.insert(orders);
        // 在往订单明细表插入n条数据
        // 因为这个订单明细表是根据当前的购物车传递过来菜品数据传递过来的几条
        // 那就传递几条
        // 所以可以便利一下这个数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            // 把当前数据都传递到orderDetail
            BeanUtils.copyProperties(cart,orderDetail);
            // 把没有的数据传递过来
            orderDetail.setOrderId(orders.getId());
            // 在把当前数据传递给一个list来传递
            orderDetails.add(orderDetail);
        }

        orderDetailMapper.insert(orderDetails);

        // 在把这个购物车表清空
        shoppingCartMapper.deleteId(userId);

        // 在拼接OrderSubmitVO
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return orderSubmitVO;
    }
}
