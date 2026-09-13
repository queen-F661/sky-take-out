package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

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
}
