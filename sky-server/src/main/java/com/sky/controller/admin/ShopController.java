package com.sky.controller.admin;

import com.sky.config.RedisConfiguration;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺的营业状态:{}",status == 1 ? "营业中" : "打样中");
        // 设置当前的redis  把当前status里面存储到redis中
        redisTemplate.opsForValue().set(key,status);

        return Result.success();
    }

    @GetMapping("/status")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(key);
        log.info("查询店铺的营业状态{}",status == 1 ? "营业中" : "打样中");

        return Result.success(status);
    }

}
