package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id来查询菜品
     * */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){

        // 现在需要一个功能
        // 是把数据从redis取出来
        // 因为怕人太多了 会导致数据库压力过大,所以把数据存储到这个里面去redis

        // 如果有就直接存入
        // key value
        // key的存入方式为 dish_ + 当前的分类id
        String key = "dish_" + categoryId;
        // 这个是获取当前的值  是根据当前的key来获取value
        List<DishVO> cat = (List<DishVO>) redisTemplate.opsForValue().get(key);

        // 如果当前拿取的值不为空且大于0
        if ( cat != null && cat.size() > 0){
            // 那么就可以把值直接传递过去
            return Result.success(cat);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        // 查看当前起售当中的菜品
        dish.setStatus(StatusConstant.ENABLE);

        List<DishVO> list = dishService.listWithFlavor(dish);
        // 如果没有数据 那么就可以通过数据库的方式存入当前的数据放到redis
        // 通过这个set方式来进行存入
        redisTemplate.opsForValue().set(key,list);

        return Result.success(list);
    }
}
