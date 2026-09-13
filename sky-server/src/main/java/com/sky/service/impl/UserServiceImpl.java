package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.util.Json;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录功能
     * */
    @Override
    public UserLoginVO Login(UserLoginDTO userLoginDTO) {
        User user = new User();
        String openId = getOpenId(userLoginDTO.getCode());
        // 在判断当前的opneId为不为空
        // 如果为空就进行判断抛出异常
        if (openId == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 如果不为空 判断当前的这条数据是不是新创建出来的
        // 如果新创建出来的就插入到数据库当中
        Integer selectopenIDcount = userMapper.openIdSelectCount(openId);
        if (selectopenIDcount > 0){
            // 这个是有值的时候
            // 那么就直接拿取数据
            user = userMapper.openIdSelect(openId);
        } else if (selectopenIDcount <= 0) {
            //如果有就返回给前端
            // 把当前的openID传递过去
            user.setOpenid(openId);
            user.setCreateTime(LocalDateTime.now());

            // 这种情况就是没有数据
            userMapper.openIdInsert(user);

            userMapper.openIdSelect(openId);
        }

        // 把当前饿
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId",user.getId());
        String jwt = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                hashMap
        );


        // 把当前需要的UserLoginVO返回给Controller
        return  UserLoginVO.builder()
                .id(user.getId())
                .openid(openId)
                .token(jwt)
                .build()
                ;

    }

    public String getOpenId(String code){
        // 先获取openId
        // 使用工具类来进行数据来发送数据
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",code);
        paramMap.put("grant_type","authorization_code");
        // 发送请求 get
        String resultObject = HttpClientUtil.doGet(WX_LOGIN, paramMap);
        // 使用这个对象解析这个字符串(json格式数据)
        JSONObject jsonObject = JSON.parseObject(resultObject);
        // 在拿取真正的openId
        String openId = jsonObject.getString("openid");

        return openId;
    }
}
