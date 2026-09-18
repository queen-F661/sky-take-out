package com.sky.controller.user;

import com.sky.dto.AddressBookDTO;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 新增地址
     * */
    @PostMapping
    public Result<Object> add(@RequestBody AddressBook addressBook){
        log.info("新增地址{}",addressBook);

        // 把数据传递给Service
        addressBookService.add(addressBook);

        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址信息
     * */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){

        List<AddressBook> list = addressBookService.list();

        return Result.success(list);
    }

    /**
     * 设置默认地址
     * */
    @PutMapping("/default")
    public Result<Object> setdefault(@RequestBody AddressBookDTO addressBookDTO){
        log.info("传递过来的数据{} 设置默认地址的id",addressBookDTO);

        addressBookService.setdefault(addressBookDTO.getId());

        return Result.success();
    }

    /**
     * 根据id来查询地址
     * */
    @GetMapping("/{id}")
    public Result<AddressBook> listById(@PathVariable Long id){
        AddressBook list = addressBookService.listById(id);

        return Result.success(list);
    }

    /**
     * 修改数据
     * */
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);

        return Result.success();
    }

    /**
     * 默认数据回显
     * */
    @GetMapping("/default")
    public Result default_listId(){
        AddressBook addressBook = addressBookService.default_listId();
        return Result.success(addressBook);
    }

    /**
     * 根据id删除地址
     * */
    @DeleteMapping
    public Result delete(Long id){

        addressBookService.delete(id);

        return Result.success();
    }
}
