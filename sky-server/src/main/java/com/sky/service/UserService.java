package com.sky.service;


import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     * 登录功能
     * */
    UserLoginVO Login(UserLoginDTO userLoginDTO);
}
