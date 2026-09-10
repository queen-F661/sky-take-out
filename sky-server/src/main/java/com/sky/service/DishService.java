package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 增加菜品接口
     * */
    void AddDish(DishDTO dishDTO);

    /**
     * 分页查询
     * */
    PageResult PageList(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除
     * */
    void deleteById(List<Long> ids);

    /**
     * 根据id来查询数据
     * */
    DishVO getById(Long id);

    /**
     * 根据当前的disDTO进行修改
     * */
    void updateId(DishDTO dishDTO);


}
