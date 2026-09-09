package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     * */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传的参数是{}",file);
        // 名称自定义组合
        String originalFilename = file.getOriginalFilename();

        String substring = null;
        if (originalFilename != null) {
            substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        UUID uuid = UUID.randomUUID();
        try {
            String objectName = uuid + substring;
            // 通过这个方法来进行文件上传
            String upload = aliOssUtil.upload(file.getBytes(), objectName);
            // 把这个返回前端
            return Result.success(upload);
        } catch (Exception e) {
            log.info("文件上传报错{}",e);
            return Result.error("报错");
        }
    }



}
