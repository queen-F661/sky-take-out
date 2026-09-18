package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 添加地址
     * */
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label) " +
            "values (#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label});")
    void add(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * */
    @Select("select * from address_book where user_id = #{currentId};")
    List<AddressBook> list(Long currentId);

    /**
     * 设置默认地址
     * */
    @Update("update address_book set is_default = 1 where id = #{id}")
    void setdefault(Long id);

    /**
     * 设置所有当前用户默认地址不开启
     * */
    @Update("update address_book set is_default = 0 where user_id = #{currentId}")
    void allsetdefault(Long currentId);

    /**
     *  根据id来查询地址
     * */
    @Select("select * from address_book where id = #{id}")
    AddressBook listById(Long id);

    /**
     * 修改数据
     * */
    void update(AddressBook addressBook);

    /**
     * 默认数据回显
     * */
    @Select("select * from address_book where is_default = 1;")
    AddressBook default_listId();

    /**
     * 根据id删除地址
     * */
    @Delete("delete from address_book where id = #{id};")
    void delete(Long id);
}
