package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;


public interface SetMealService {

    /**
     * 分页查询
     * */
    PageResult pageList(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增查询
     * */
    void add(SetmealDTO setmealDTO);

    /**
     * 删除
     * */
    void delete(List<Long> ids);

    /**
     * 根据id来查询
     * */
    SetmealVO getById(String id);

    /**
     * 套餐修改接口
     * */
    void update(SetmealDTO setmealDTO);
}
