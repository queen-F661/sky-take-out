package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("分类查询相关接口")
@RequestMapping("/admin/category")
@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 分页查询
     * @params categoryPageQueryDTO
     * */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result PageList(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("传递过来的数据{}",categoryPageQueryDTO);

        PageResult pageList = categoryService.pageList(categoryPageQueryDTO);

        return Result.success(pageList);
    }

    /**
     * 启用禁用分类
     * */
    @PostMapping("/status/{status}")
    @ApiOperation("启用,禁用分类")
    public Result StartStop(@PathVariable Integer status , Long id){
        // 首先启动禁用不需要传递给前端
        log.info("启动,禁用分类{},{}",status,id);
        categoryService.startStop(status,id);
        return Result.success();
    }

    /**
     * 修改分类
     * */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类{}",categoryDTO);

        categoryService.update(categoryDTO);

        return Result.success();
    }

    /**
     * 新增分类
     * */
    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 删除功能
     * */
    @DeleteMapping
    @ApiOperation("根据id删除")
    public Result delete(Long id){

        categoryService.delete(id);

        return Result.success();
    }
}
