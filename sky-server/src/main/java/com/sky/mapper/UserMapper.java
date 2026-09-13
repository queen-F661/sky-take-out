package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 查询openID
     * 如果查询出来 就返回
     * */
    @Select("select count(*) from user where openid = #{openId};")
    Integer openIdSelectCount(String openId);

    @Select("select * from user where openid = #{openId};")
    User openIdSelect(String openId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user(openid, name, phone, sex, id_number, avatar, create_time) " +
            "VALUES (#{openid},#{name},#{phone},#{sex},#{idNumber},#{avatar},#{createTime})")
    void openIdInsert(User user);
}
