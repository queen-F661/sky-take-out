package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 分页查询
     * */
    @GetMapping("/page")
    public Result<PageResult> pageList(SetmealPageQueryDTO setmealPageQueryDTO){

        // 把数据打印出来
        log.info("分页查询{}",setmealPageQueryDTO);

        // 把数据传递给service
        PageResult pageResult = setMealService.pageList(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * */
    @PostMapping
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("新增菜单");

        // 把数据传递到当前的service
        setMealService.add(setmealDTO);

        return Result.success();
    }
}
