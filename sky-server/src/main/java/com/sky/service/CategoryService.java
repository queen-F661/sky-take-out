package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {

    /**
     * 分页查询
     * */
    PageResult pageList(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * */
    void startStop(Integer status , Long id);

    /**
     * 修改分类
     * */
    void update(CategoryDTO categoryDTO);

    /**
     * 新增分类
     * */
    void save(CategoryDTO categoryDTO);
}
