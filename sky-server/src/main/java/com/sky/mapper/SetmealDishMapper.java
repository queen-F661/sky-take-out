package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    Integer getId(List<Long> ids);

    /**
     * 新增菜品套餐关系表
     * */
    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies)\n" +
            "values (#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void add(SetmealDish setmealDish);
}
