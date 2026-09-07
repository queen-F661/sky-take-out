package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface DishMapper {
    /**
     * 根据id来查询当前套餐下面有没有数据 如果有数据 就返回给Service端进行判断
     * 如果有值的话 就不能返回
     *
     * 总结就是返回一个总数
     * */
    @Select("SELECT count(*) FROM dish where category_id = #{id}")
    Integer countByCategoryId(Long id);
}
