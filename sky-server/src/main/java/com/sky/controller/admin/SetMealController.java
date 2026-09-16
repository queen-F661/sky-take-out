package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
    // 当在当前分类下面添加一个套餐  根据这个分类id来删除相应相应套餐数据
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
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
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){

        setMealService.delete(ids);

        return Result.success();
    }

    /**
     * 数据回显
     * */
    @GetMapping("/{id}")
    public Result<SetmealVO> getId(@PathVariable String id){

        log.info("数据回显{}",id);

        SetmealVO setmealVO = setMealService.getById(id);

        return Result.success(setmealVO);
    }

    /**
     * 套餐修改接口
     * */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){

        log.info("套餐修改接口{}",setmealDTO);
        setMealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售、停售
     * */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id){

        log.info("套餐起售、停售传递过来的数据{}",status,id);
        setMealService.status(status,id);

        return Result.success();
    }
}
