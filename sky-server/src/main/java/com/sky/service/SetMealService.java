package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetMealService {

    /**
     * 分页查询
     * */
    PageResult pageList(SetmealPageQueryDTO setmealPageQueryDTO);
}
