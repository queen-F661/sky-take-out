package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @GetMapping("/page")
    public Result<PageResult> PageList(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询所查询的数据{}",dishPageQueryDTO);

        PageResult pageResult = dishService.PageList(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 菜品删除接口
     * @RequestParam 这个注解可以自动的帮助当前的ids来进行逗号隔离
     * */
    @DeleteMapping
    public Result<Object> deleteId(@RequestParam List<Long> ids){
        log.info("菜品删除接口{}",ids);

        dishService.deleteById(ids);
        return Result.success();
    }

    /**
     * 数据回显
     * 相当于是根据id来进行查询菜品
     * */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("数据回显拿取的id{}",id);

        DishVO dishVO = dishService.getById(id);

        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * */
    @PutMapping
    public Result updateId(@RequestBody DishDTO dishDTO){

        log.info("修改菜品{}",dishDTO);

        dishService.updateId(dishDTO);
        return Result.success();
    }
}
