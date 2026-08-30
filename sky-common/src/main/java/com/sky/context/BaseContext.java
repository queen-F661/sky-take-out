package com.sky.context;

/**
 * 将ThreadLocal定义为静态变量放到本工具类，全局可访问
 * 用来保存当前登录用户id，一次请求全程共用一条线程，
 * 在拦截器存入，Controller、Service、Mapper任意层都可以取出
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
