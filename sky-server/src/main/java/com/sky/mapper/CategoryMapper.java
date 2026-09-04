package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询
     * */
    // @Select("select * from category where name = #{name} and type = #{type}")
    List<Category> getList(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新数据
     * */
    void update(Category build);
}
