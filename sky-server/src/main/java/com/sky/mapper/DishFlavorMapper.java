package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    public void AddDish(List<DishFlavor> flavors);

    /**
     * 根据当前菜品id来删除口味
     * */
    @Delete("delete from dish_flavor where dish_id = #{dishID}")
    void deleteDish_id(Long dishID);
}
