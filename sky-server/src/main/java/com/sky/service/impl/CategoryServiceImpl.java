package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     分为二步  一个是总数 一个是当前返回的集合List集合数据众和
     使用PageHelper框架  直接使用PageHelper
     这个PageHelper.startPage 是用来标记下一次Mapper
     PageHelper 这个是用查找标记的
     */
    @Override
    public PageResult pageList(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> pageHelper = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        List<Category> category = categoryMapper.getList(categoryPageQueryDTO);

        // 把数据取出来 在进行拼接
        long total = pageHelper.getTotal();
        List<Category> result = pageHelper.getResult();
        return new PageResult(total,result);
    }
}
