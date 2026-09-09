package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    /**
     * 增加菜品接口
     * */
    @PostMapping
    @ApiOperation("增加菜品接口")
    public Result<Object> AddDish(@RequestBody DishDTO dishDTO){
        log.info("增加菜品{}",dishDTO);

        // 通过这个数据传递
        dishService.AddDish(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * */
//    @GetMapping
//    public Result<PageResult> PageList(DishPageQueryDTO dishPageQueryDTO){
//        log.info("分页查询所查询的数据{}",dishPageQueryDTO);
//
//        PageResult pageResult = dishService.PageList(dishPageQueryDTO);
//
//        return Result.success(pageResult);
//    }
}
