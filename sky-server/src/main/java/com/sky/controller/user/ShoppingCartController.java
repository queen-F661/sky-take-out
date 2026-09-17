package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 展示当前的数据
     * */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        // 根据当前的baseContext查询相关的信息
        List<ShoppingCart> shoppingCarts = shoppingCartService.list();

        return Result.success(shoppingCarts);
    }

    /**
     * 清空购物车
     * */
    @DeleteMapping("/clean")
    public Result<Object> delete(){

        shoppingCartService.delete();

        return Result.success();
    }

    /**
     * 删除购物车的一个商品
     * */
    @PostMapping("/sub")
    public Result<Object> deleteId(@RequestBody ShoppingCartDTO shoppingCartDTO){

        shoppingCartService.deleteId(shoppingCartDTO);

        return Result.success();
    }
}
