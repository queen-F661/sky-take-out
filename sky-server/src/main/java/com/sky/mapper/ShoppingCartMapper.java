package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 动态条件查询
     * */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新当前的数量
     * */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart shoppingCart);

    /**
     * 插入一条数据
     * */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, number, create_time) " +
            "values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{number},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * */
    @Delete("delete from shopping_cart where  user_id = #{userId};")
    void deleteId(Long userId);

    /**
     * 查询number
     * */
    Integer dishCount(ShoppingCart shoppingCart);

    /**
     * 根据当前的id来进行删除
     * */
    void delete(ShoppingCart shoppingCart);

    /**
     * 删除number数量
     * */
    void deleteNumber(ShoppingCart shoppingCart);
}
