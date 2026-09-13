package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 删除套餐关系表
     * */
    void delete(List<Long> ids);


    /**
     * 根据setmeal_id来查询数据
     * 数据回显
     * */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId};")
    List<SetmealDish> getById(String setmealId);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteId(Long id);

}
