package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

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

    /**
     * 根据id删除
     * */
    void delete(Long id);

    /**
     * 根据type来查询分类数据
     *
     * @return
     *
     */
    List<Category> list(Integer type);
}
