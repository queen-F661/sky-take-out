package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOSSConfiguration {

    // 我传入的是当前的四个在yml的参数
    @Bean
    public AliOssUtil aliyun(AliOssProperties aliOssProperties){
        // 将这四个传入到当前的util
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName()
                );
    }

}
