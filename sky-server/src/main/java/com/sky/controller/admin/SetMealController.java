package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        log.info("新增套餐");

        // 把数据传递到当前的service
        setMealService.add(setmealDTO);

        return Result.success();
    }

    /**
     * 批量删除套餐
     * @RequestParam 这个注解可以自动的帮助当前的ids来进行逗号隔离
     * */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){

        setMealService.delete(ids);

        return Result.success();
    }
}
