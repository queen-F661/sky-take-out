package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public void startStop(Integer status , Long id) {
        // 这个为了扩张  创建一个类  通过这个类来写动态sql  下次遇到更新可以直接调用
//        Category category = new Category();
//
//        category.setId(id);
//        category.setStatus(status);
          Category build = Category.builder()
                .id(id)
                .status(status)
                .build();

          categoryMapper.update(build);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        // 修改分类  因为这个里面只需要改二个字段
        // 但是有一些公共字段要手动修改
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        // 手动修改更新时间和修改人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        // 在把这个category传递过Mapper端
        categoryMapper.update(category);
    }


}