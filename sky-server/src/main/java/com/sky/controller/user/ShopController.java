package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

// 可以在这个@RestController里面去定义一个bean的名称
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    public Result getStatus(){
        // Redis 里 JDK 序列化存的可能是 Integer 也可能是 Long，统一按 Number 取 intValue()，避免 ClassCastException
        Object value = redisTemplate.opsForValue().get(key);
        Integer status = value == null ? 0 : ((Number) value).intValue();
        log.info("查询店铺的营业状态{}", status == 1 ? "营业中" : "打烊中");

        return Result.success(status);
    }

}
