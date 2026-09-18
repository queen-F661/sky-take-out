package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;


public interface AddressBookService {

    /**
     * 新增地址
     * */
    void add(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * */
    List<AddressBook> list();

    /**
     * 设置默认地址
     * */
    void setdefault(Long id);

    /**
     * 根据id来查询地址
     * */
    AddressBook listById(Long id);

    /**
     * 修改数据
     * */
    void update(AddressBook addressBook);

    /**
     * 默认数据回显
     * */
    AddressBook default_listId();

    /**
     * 根据id删除地址
     * */
    void delete(Long id);
}
