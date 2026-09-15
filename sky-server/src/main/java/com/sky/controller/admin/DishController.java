package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增加菜品接口
     * */
    @PostMapping
    @ApiOperation("增加菜品接口")
    public Result<Object> AddDish(@RequestBody DishDTO dishDTO){
        log.info("增加菜品{}",dishDTO);

        // 通过这个数据传递
        dishService.AddDish(dishDTO);


//        // 清理我们的缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
//        // 删除当前的redis相应的数据
//        redisTemplate.delete(key);
        cleanCache(key);

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

//        // 这个是查询当前的key为dish_开头的
//        Set keys = redisTemplate.keys("dish_*");
//        // 这个是删除当前redis下面的dish开头的数据
//        redisTemplate.delete(keys);
        cleanCache("dish_*");
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

//        // 这个是查询当前的key为dish_开头的
//        Set keys = redisTemplate.keys("dish_*");
//        // 这个是删除当前redis下面的dish开头的数据
//        redisTemplate.delete(keys);

        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 套餐新增页面的
     * 添加菜品
     * */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId){
        log.info("菜品查询:{}",categoryId);

        // 因为前端要获取多条数据  所以要封装当前的数据
        List<Dish> list = dishService.list(categoryId);
        cleanCache("dish_*");

        return Result.success(list);

    }

    /**
     * 清理缓存数据
     * */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
