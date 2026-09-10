package com.sky.mapper;

import com.sky.annoation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 新增菜品
     * */
    @AutoFill(value = OperationType.INSERT )
    public void AddDish(List<DishFlavor> flavors);

    /**
     * 根据当前菜品id来删除口味
     * */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteDish_id(Long dishId);

    void deleteDish_ids(List<Long> dishId);

    /**
     * 根据当前的id来进行查询菜品口味
     * */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getById(Long dishId);

}
