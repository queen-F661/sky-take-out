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

    @Override
    public void save(CategoryDTO categoryDTO) {

        // 先把数据取出来 放到一个完整的数据表对于的类 entity
        Category category = new Category();

        // 把当前从数据库取出来的数据放到category
        BeanUtils.copyProperties(categoryDTO,category);

        // 在把基础的字段  默认的填进去
        // 开店 默认为关闭的  所以save这个字段设置为0
        category.setStatus(0);

        // 默认设置当前时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        // 设置当前创建人和更新人为的id为当前登录的用户
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        // 在把这个对象传递给mapper端 更新数据
        categoryMapper.save(category);
    }

    @Override
    public void delete(Long id) {

        categoryMapper.delete(id);

    }


}