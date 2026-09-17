package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {


    /**
     * 新增购物车数据
     * */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 页面展示
     * */
    List<ShoppingCart> list();

    /**
     * 清空购物车
     * */
    void delete();
}
