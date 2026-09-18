package com.sky.service.impl;

import ch.qos.logback.core.util.ContextUtil;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import java.util.Collections;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * */
    @Override
    public void add(AddressBook addressBook) {

        // 因为当前的数据肯定是
        // 拿取当前的userId给当前的这个对象
        // 通过给这个对象数据 来进行封装
        Long currentId = BaseContext.getCurrentId();
        addressBook.setUserId(currentId);

        // 还要设置默认值  因为这个地址表有一个默认地址
        // 但是呢 所以默认为0
        addressBook.setIsDefault(0);

        // 在把当前的值传递给mapper
        addressBookMapper.add(addressBook);
    }

    /**
     * 查询当前登录用户的所有地址信息
     * */
    @Override
    public List<AddressBook> list() {
        // 根据当前的userId来进行查询用户相关的数据
        Long currentId = BaseContext.getCurrentId();

        List<AddressBook> list = addressBookMapper.list(currentId);

        return list;
    }

    /**
     * 设置默认地址
     * */
    @Override
    public void setdefault(Long id) {
        // 根据当前的userId来进行查询用户相关的数据
        Long currentId = BaseContext.getCurrentId();
        addressBookMapper.allsetdefault(currentId);
        // 本质就是一个更新
        addressBookMapper.setdefault(id);
        // 如果这个值设置为1之后 其他的都需要修改成0

    }

    /**
     * 根据id来查询地址
     * */
    @Override
    public AddressBook listById(Long id) {

        AddressBook list = addressBookMapper.listById(id);
        return list;
    }

    /**
     * 修改数据
     * */
    @Override
    public void update(AddressBook addressBook) {
        // 把数据传入当前的mapper里面
        // 使用动态sql的方式存入
        addressBookMapper.update(addressBook);
    }

    @Override
    public AddressBook default_listId() {
        // 默认数据回显
        // 直接根据当前的1
        AddressBook addressBook = addressBookMapper.default_listId();

        return addressBook;
    }

    /**
     * 根据id删除地址
     * */
    @Override
    public void delete(Long id) {

        addressBookMapper.delete(id);
    }
}
