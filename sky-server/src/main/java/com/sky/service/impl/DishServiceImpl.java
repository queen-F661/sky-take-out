package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
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

    /**
     * 菜单删除
     * */
    @Override
    @Transactional
    public void deleteById(List<Long> ids) {
        // 国五关 斩六将

        // 判断当前什么情况不能删除
        // 第一种 - 起售品不能删除  使用查询的手段判断当前为不为起售品
        for (Long id : ids) {
            Integer Status = dishMapper.getByIdstatus(id);
            if (Status == 1){
                // new throws
                throw new DeletionNotAllowedException("起售中的菜品不能删除");
            }
        }

        // 第二种 - 被套餐关联的菜品也不能删除
        // 如果查询出当前的套餐id 说明这个里面还是有数据的
        Integer count = setmealDishMapper.getId(ids);
        if(count > 0){
            // 写一个throws
            throw new DeletionNotAllowedException("被套餐关联的菜品也不能删除");
        }

        // 删除当前的菜品表数据
//        for (Long id : ids) {
//            dishMapper.deleteId(id);
//            // 删除口味表数据 根据当前菜品表的id来删除口味表
//            dishFlavorMapper.deleteDish_id(id);
//        }

        // 优化这二个方法
        // 根据ids来进行批量删除
        dishMapper.deleteIds(ids);

        // 根据当前菜品表的id来删除口味表中的数据
        dishFlavorMapper.deleteDish_ids(ids);

    }

    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = new DishVO();
        // 根据id来进行查询  一共查询二张表
        // 一张是当前的菜品表
        Dish dish = dishMapper.getById(id);

        // 还有一张就是菜品口味表
        List<DishFlavor> flavors = dishFlavorMapper.getById(id);


        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }
    @Transactional
    @Override
    public void updateId(DishDTO dishDTO) {
        // 分为二步
        // 一步是修改本类的
        // 所以要使用dish的实体类来接收数据 进行修改
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateId(dish);

        // 第二步是修改菜品口味表
        // 因为这个比较复杂
        // 所以分二步
        // 第一步是把当前的数据进行删除
        dishFlavorMapper.deleteDish_id(dishDTO.getId());

        // 第二部是把当前的数据进行添加进去
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && flavors.size() > 0){
            for (DishFlavor dishFlavor : flavors){
                dishFlavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.AddDish(flavors);
        }
    }
}
