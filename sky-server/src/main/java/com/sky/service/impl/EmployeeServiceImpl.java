package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 先把密码MD5 加密
        // 在进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    @Override
    public void save(EmployeeDTO employeeDTO) {
        // 自创部分
//        // 把数据传递给entity
//        // 把当前数据进行拆解放到
//        String username = employeeDTO.getUsername();  // 账号
//        String name = employeeDTO.getName(); // 姓名
//        String phone = employeeDTO.getPhone(); // 手机号
//        String sex = employeeDTO.getSex();  // 性别
//        String idNumber = employeeDTO.getIdNumber(); // 身份证
//
//
//        String password = "123456";  // 默认密码
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        LocalDateTime createTime = LocalDateTime.now();  // 创建时间
//        LocalDateTime updateTime = LocalDateTime.now();  // 更新时间
//        Integer status = 1;  // 设置状态
//
//        // 当前的更新人和创建人
//        Long createUser = 10L;
//
//        Long updateUser = 1L;
//
//        // 把值封装成entity
//        Employee employee = new Employee(employeeDTO.getId(), username, name, password, phone, sex, idNumber, status, createTime, updateTime, createUser, updateUser);
//        log.info("封装成entity后{}",employee);
//        employeeMapper.save(employee);


        // 老师讲的
        Employee employee = new Employee();

        // 对象属性拷贝
        // 前面拷贝后面  前面相当于圆  后面相当于是目标
        BeanUtils.copyProperties(employeeDTO,employee);

        // 设置默认正常的状态
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码 因为前端没有传递密码,只能先是一个默认的密码
        // 要使用MD5进行加密
        employee.setPassword( DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置当前创建人的id和修改人的id
        // TODO 这个功能还没有写全  改为当前登录用户的id
        employee.setCreateUser(10L);
        employee.setUpdateUser(10L);

        // 在把当前这个对象传递给Mapper
        // 让mapper端进行
        employeeMapper.save(employee);
    }
}
