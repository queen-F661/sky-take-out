package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Page<SetmealVO> setmealDTO = setmealMapper.PageList(setmealPageQueryDTO);

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
            setmealDish.setDishId(id);

            setmealDishMapper.add(setmealDish);
        }
    }
}
