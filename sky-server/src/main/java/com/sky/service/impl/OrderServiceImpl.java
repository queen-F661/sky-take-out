package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.webSocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private WebSocketServer webSocketServer;

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

    /**
     * 模拟支付成功
     * 原课程流程：前端点支付 -> 后端 WeChatPayUtil 调微信统一下单 -> 前端拉起微信收银台
     *            -> 用户付款 -> 微信服务器回调后端 -> 后端修改订单状态
     * 模拟流程：因为没有商户号/商户证书/认证小程序，微信那一整段直接跳过，
     *          用户点支付时，后端"替微信"把订单标记为已支付，效果和真支付成功一样
     * */
    @Override
    public void paySuccess(String orderNumber) {
        // 第一步：根据订单号把订单查出来，查不到说明订单有问题，直接抛业务异常
        Orders ordersDB = orderMapper.getByNumber(orderNumber);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 第二步：模拟"微信支付成功回调"要做的事 —— 只改 3 个字段
        // 这里只 new 一个新对象来装要改的字段，千万不要直接用 ordersDB 整个更新
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());                  // 按主键定位这一行
        orders.setPayStatus(Orders.PAID);                // 支付状态：0未支付 -> 1已支付
        orders.setStatus(Orders.TO_BE_CONFIRMED);        // 订单状态：1待付款 -> 2待接单（商家端才能看到这单）
        orders.setCheckoutTime(LocalDateTime.now());     // 付款时间：记录当前时间
        orderMapper.update(orders);

        // 使用webSocket 用来给当前的管理端发送消息
        // type orderId content
        HashMap map = new HashMap();
        map.put("type",1);
        map.put("orderId",ordersDB.getId());
        map.put("content","订单号:" + orderNumber);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    @Override
    public void reminder(Long id) {
        Orders byId = orderMapper.getById(id);
        if(byId == null){
            throw new AddressBookBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("type",2);
        hashMap.put("orderId",id);
        hashMap.put("content","订单号" + byId.getNumber());

        webSocketServer.sendToAllClient(JSON.toJSONString(hashMap));
    }

    @Override
    public PageResult history(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 先进行分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        // 因为你查询历史记录肯定要根据当前用户来查询
        // 如果不怎么查的话数据会在一起
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        ArrayList<OrderVO> orderVOS = new ArrayList<>();
        // 在根据当前循环来进行数据的便利和获取
        for (Orders orders : page) {
            // 首先 你肯定是要当前这个主表的数据id取出来
            Long orderId = orders.getId();
            // 在执行sql 来进行数据的查询 看每一条地址数据有几条明细数据
            // 因为有时候明细表会查询几条数据 所以要使用当前的list集合进行存储
            List<OrderDetail> OrderDetail = orderDetailMapper.pageQuery(orderId);

            // 创建一个对象 用来存储数据在给循环外面的数据存储数据
            OrderVO orderVO = new OrderVO();
            // 把当亲的对象存储到orders里面
            BeanUtils.copyProperties(orders,orderVO);
            // 把这个明细表存储到这个orderVo的一个字段上面
            orderVO.setOrderDetailList(OrderDetail);
            // 给外面的集合添加值
            orderVOS.add(orderVO);
        }

        // 在进行最后的拼接
        // 获取当前的总数
        long total = page.getTotal();

        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(orderVOS);

        return pageResult;
    }

    /**
     * 查询订单详情
     * */
    @Override
    public OrderVO detail(Long id) {

        // 首先 要根据当前的id来进行查询值
        // 但是 肯定是要在本用户里面
        // 所以 传递二个值进行根据用户id的查询
        Orders orders = orderMapper.getById(id);

        // 在进行根据当前的地址id来查询地址明细表的数据
        List<OrderDetail> orderDetails = orderDetailMapper.pageQuery(id);

        // 在进行组装返回
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderDetailList(orderDetails);
        BeanUtils.copyProperties(orders,orderVO);

        return orderVO;
    }

    /**
     * 取消订单
     * */
    @Override
    public void cancel(Long id) {

        orderMapper.cancel(id);

    }


}
