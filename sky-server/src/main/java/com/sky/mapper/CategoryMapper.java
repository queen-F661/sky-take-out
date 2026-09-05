package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
     * 更新分类
     * */
    void update(Category build);

    /**
     * 创建分类
     * */
    @Insert("INSERT INTO category(type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}; ")
    void delete(Long id);
}
