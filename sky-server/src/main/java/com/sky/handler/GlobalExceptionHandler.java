package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理sql异常
     * */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();

        // 判断当前异常前面是不是这个异常 Duplicate entry 'test01' for key 'employee.idx_username'
        // 因为这个contains是判断当前字符串是不是包含这个一小段  如果是使用equals()
        // 就必须一摸一样
        if (message.contains("Duplicate entry")){
            // 使用字符串的split来进行分割 获取当当前用户名的名称
            String[] split = message.split(" ");
            // 因为这个名称是在第三段  根据下标是从0开始  那么下标为2
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            log.info("{}",msg);
            // 因为这个是异常处理  肯定是报错信息
            return Result.error(msg);
        }else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
         }
       }
}
