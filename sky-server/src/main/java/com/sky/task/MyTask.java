package com.sky.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyTask {

//    @Scheduled(cron = "1/5 * * * * *")
//    public void executeTask(){
//        log.info("定时任务执行{}", new Date());
//    }

}
