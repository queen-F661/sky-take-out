package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 新增购物车数据
     * */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody ShoppingCartDTO shoppingCartDTO){

        shoppingCartService.add(shoppingCartDTO);
        // 因为这个是新增 不需要传递数据
        return Result.success();
    }
}
