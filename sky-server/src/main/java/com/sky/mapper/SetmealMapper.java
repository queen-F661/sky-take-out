package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annoation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * 根据id来查询当前套餐下面有没有数据 如果有数据 就返回给Service端进行判断
     * 如果有值的话 就不能返回
     *
     * 总结就是返回一个总数
     * */
    @Select("SELECT count(*) FROM setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);

    /**
     * 分页查询
     * */
    Page<SetmealVO> PageList(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into setmeal(category_id, name, price, description, image, create_time, update_time, create_user, update_user) " +
            "VALUES (#{categoryId},#{name},#{price},#{description},#{image},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Setmeal setmeal);
}
