package com.sky.enumeration;

import com.sky.annoation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import javassist.bytecode.SignatureAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    // 首先  定义一个切入点
    // 这个是必须二个条件  一个是当前mapper类   还有一个是标记为AutoFill() 这个类
    // 这个筛选出来的方法就是切入点  没有就是连接点
    //
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annoation.AutoFill)")
    public void autoFill(){};

    // 通知  这个是逻辑的最终执行方法
    @Before("autoFill()")
    public void before(JoinPoint joinPoint){
        log.info("开始字段的自动填充....");


        // 先获取对象的数据类型  是INSERT 还是UPDATE
        // 方法签名对象(这个是获取相应的方法名称 返回对象 还有相应的名称)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取相应的注解对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        // 在通过这个注解里面 获得相应的数据类型
        OperationType value = annotation.value();

        // 在获取相应的实体类  要通过这个实体类来进行
        // 这个是获取当前方法的参数
        Object[] args = joinPoint.getArgs();

        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];
        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 根据当前对于的属性进行赋值  使用反射的手段
        if (value == OperationType.INSERT){
            // 为四个字段进行赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if (value == OperationType.UPDATE){
            // 为二个字段进行赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
