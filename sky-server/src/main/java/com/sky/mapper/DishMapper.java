package com.sky.mapper;

import com.sky.annoation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 新增菜品
     * */
    @AutoFill(OperationType.INSERT)
    void AddDish(Dish dish);

    /**
     * 分页查询（SQL 在 DishMapper.xml 的 PageList，连表 category 查出 categoryName）
     * */
    List<DishVO> PageList(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id来查询status
     * @return
     */
    @Select("select status from dish where id = #{id}")
    Integer getByIdstatus(Long id);

    /**
     * 删除当前的菜品表
     * */
    @Delete("delete from dish where id = #{id}")
    void deleteId(Long id);

    /**
     * 根据ids来进行删除
     * */
    void deleteIds(List<Long> ids);
}
