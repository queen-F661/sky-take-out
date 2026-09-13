package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Mapper;

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
}
