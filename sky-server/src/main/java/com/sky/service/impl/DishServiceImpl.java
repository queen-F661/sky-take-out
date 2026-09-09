package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 增加菜品接口
     * */
    @Override
    public void AddDish(DishDTO dishDTO) {

        // 根据当前传递过来的数据分为二个部分
        // 首先 传递当前的菜品表
        // 因为这个DTO数据当中有一个List的数据不用传递
        // 但是这个传递过来不需要  所以要传递一个entity的类
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 传递过来之后就给传递给Mapper
        dishMapper.AddDish(dish);

        // 在传递当前的菜品口味表
        // 首先  我们知道这个数据是要从当前的DTO中获取过来
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 因为这个口味表的数据不一定要传递过来  所以这个里面还需要判断一下这个数据
        // 这个数据为不为空
        if (flavors != null && flavors.size() > 0){
            Long id = dish.getId();
            for (DishFlavor dishFlavor : flavors){
                dishFlavor.setDishId(id);
            }
            dishFlavorMapper.AddDish(flavors);
        }
    }

    @Override
    public PageResult PageList(DishPageQueryDTO dishPageQueryDTO) {

        // 分为二个  一个是total 一个是records
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = (Page<DishVO>) dishMapper.PageList(dishPageQueryDTO);

        long total = page.getTotal();
        List<DishVO> result = page.getResult();
        return new PageResult(total, result);
    }
}
