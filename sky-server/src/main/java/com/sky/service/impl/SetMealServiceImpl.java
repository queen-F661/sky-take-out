package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 分页查询
     * */
    @Override
    public PageResult pageList(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 使用pageHelper
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        // 这个就可以标记当前的limit 自动的帮我注入
        Page<SetmealVO> setmealDTO = setmealMapper.pageList(setmealPageQueryDTO);

        long total = setmealDTO.getTotal();
        List<SetmealVO> result = setmealDTO.getResult();

        return new PageResult(total, result);
    }

    /**
     * 增加菜品
     * */
    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {


        // 首先 拿取当前的数据
        // 分为二个部分
        // 一个是菜单表
        // 因为是菜单表  这个里面有一个是专门的菜单表 所以要把这个数据剥离出来 给单独的放到mapper里面
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        // 在把当前的数据传递给菜单表
        setmealMapper.add(setmeal);

        // 还有一个是当前的套餐菜品表
        // 把当前的数据进行拿出来 在给返回的值赋值
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            Long id = setmeal.getId();
            setmealDish.setSetmealId(id);

            setmealDishMapper.add(setmealDish);
        }
    }
    @Transactional
    @Override
    public void delete(List<Long> ids) {

        // 这个是判断当前是不是在启售
        for (Long id : ids) {
            Integer status = setmealMapper.getStatus(id);
            // 这个是因为当前如果数据库没有查到相应的数据
            // 那么Interage会拆包 会报错 是不是这个意思
            if(status != null && status == 1){
                throw new DeletionNotAllowedException("当前的售卖状态为启售");
            }
        }


        // 这个里面不只是删除这个表
        // 还需要删除当前的菜品和套餐关系表
        // 先删除当前的套餐表
        setmealMapper.delete(ids);

        // 在根据传递过来的ids来进行删除对应的数据
        setmealDishMapper.delete(ids);
    }

    /**
     * 根据id来查询相关的数据
     * 数据的回显
     * */
    @Override
    public SetmealVO getById(String id) {

        SetmealVO setmealVO =  setmealMapper.getById(id);

        // 因为这个数据菜品也有数据  所以当前还需要查看这个数据还有没有
        // 查看当前的mapper
        List<SetmealDish> setmealDishes = setmealDishMapper.getById(id);
        setmealVO.setSetmealDishes(setmealDishes);
        log.info("数据回显后的传递给前端的数字{}",setmealVO);
        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {

        // 首先  看前端接口过来的数据为不为空
        // 如果为空 那么就直接在这里报错
        if(setmealDTO == null){
            throw new DeletionNotAllowedException("当前数据为空");
        }

        // 首先  这个是关系二张表
        // 第一个是套餐菜品表
        // 要把数据取出来
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);

        // 一个是菜品和套餐关系表
        // 首先 是把当前的数据取出
        // 因为这个数据是直接取出来

        // 要首先 你要把表直接全部删除
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long id = setmealDTO.getId();
        setmealDishMapper.deleteId(id);

        // 新增
        for (SetmealDish setmealDish : setmealDishes) {
            // 插入setmeal_id
            // 因为数据回显这个是根据这个字段查询的 如果没有这个字段 那咋查
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.add(setmealDish);
        }
    }

    /**
     * 套餐起售、停售
     * */
    @Override
    public void status(Integer status,Long id) {
        if(status == null){
            throw new DeletionNotAllowedException("传递过来数据为空null");
        }
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.update(setmeal);
    }

    /**
     * 根据id来查询套餐数据
     * */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {

        List<Setmeal> list = setmealMapper.list(setmeal);

        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> list = setmealDishMapper.getDishSetmealId(id);
        return list;
    }
}
