package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

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
}
