package com.sky.service;


import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {


    /**
     * 新增购物车数据
     * */
    void add(ShoppingCartDTO shoppingCartDTO);
}
