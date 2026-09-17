package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.utils.JwtUtil;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增购物车数据
     * */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {

        // 首先 要进行当前判断这个购物车上有没有值
        // 把当前的值传入进去给判断
        ShoppingCart shoppingCart = new ShoppingCart();
        // 在根据这个空对象  把传递过来的数据传入进去
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        // userId的获取
        // 获取当前的userID
        Long UserId = BaseContext.getCurrentId();
        // 在把数据放入到当前的实体类中
        shoppingCart.setUserId(UserId);

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        // 如果有值的话  肯定不会让他多增加一条  肯定是更新当前数据里面的数据 number
        if (shoppingCarts != null && shoppingCarts.size() > 0){
            // 因为这个我们的数据动态查询 只能去查询一条数据
            // 不会说是查询二条或者二条以上  就是根据当前的菜品id查询和套餐查询
            // 这二个
            ShoppingCart cart = shoppingCarts.get(0);

            // 所以是直接通过当前的number + 1 注入到select  注入到数据库中
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);

            // 在把数据传入传入到mapper里面
            shoppingCartMapper.updateNumber(cart);
        }else {
            // 如果说是没有值 那就根据当前的数据来新增一条
            Long dishId = shoppingCart.getDishId();
            Long setmealId = shoppingCart.getSetmealId();

            // 根据当前的菜品id 或者说是 套餐id来查看当前存入这个购物车是不是菜品还是套餐
            if(dishId != null){
                // 如果当前的菜品id不为空 那么就知道当前数据为菜品
                Dish byId = dishMapper.getById(dishId);
                // 那么把当前拿出来的数据传入到购物车表上面
                shoppingCart.setName(byId.getName());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());

                // 设置当前的固定值
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());

            }else {
                // 如果当前的菜品id不为空 那么就知道当前数据为套餐
                SetmealVO byId = setmealMapper.getById(setmealId);
                // 那么把当前拿出来的数据传入到购物车表上面
                shoppingCart.setName(byId.getName());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());

            }
            // 设置当前的固定值
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            // 在把当前的数据传入到sql
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 页面展示
     * */
    @Override
    public List<ShoppingCart> list() {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(currentId);
        // 把当前需要的数据取出来
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

}
