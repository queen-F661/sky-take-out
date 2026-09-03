package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {

    /**
     * 分页查询
     * */
    PageResult pageList(CategoryPageQueryDTO categoryPageQueryDTO);
}
