package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
//    @PostMapping("status/{status}")
//    @ApiOperation("启用,禁用分类")
//    public Result StartStop(@PathVariable Long status , Long id){
//        // 首先启动禁用不需要传递给前端
//        log.info("启动,禁用分类{},{}",status,id);
//
//        return Result.success();
//    }
}
