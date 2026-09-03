package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

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
        System.out.println("当前线程的id:" + Thread.currentThread().getId());

        Employee employee = new Employee();

        // 对象属性拷贝
        // 前面拷贝后面  前面相当于圆  后面相当于是目标
        BeanUtils.copyProperties(employeeDTO,employee);

        // 设置默认正常的状态
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码 因为前端没有传递密码,只能先是一个默认的密码
        // 要使用MD5进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置当前创建人的id和修改人的id
        // 从ThreadLocal中获取当前的id
        System.out.println("当前线程的id:" + Thread.currentThread().getId());
        Long currentId = BaseContext.getCurrentId();

        if(currentId !=null){
            employee.setCreateUser(currentId);
            employee.setUpdateUser(currentId);
        }

        BaseContext.removeCurrentId();
        // 在把当前这个对象传递给Mapper
        // 让mapper端进行
        employeeMapper.save(employee);
    }

    @Override
    public PageResult pageList(EmployeePageQueryDTO employeePageQueryDTO) {
////        // 先计算当前的起始位置
////        // 首先  这个是String类型,先转成Int类型
////        Integer pageNum = Integer.parseInt(String.valueOf(employeePageQueryDTO.getPage()));
////        Integer pageSizeNum = Integer.parseInt(String.valueOf(employeePageQueryDTO.getPageSize()));
//        int pageNum = employeePageQueryDTO.getPage();
//        int pageSizeNum = employeePageQueryDTO.getPageSize();
//        //在通过计算出当前的页码起始位置
//        Integer beginIndex = (pageNum - 1) * pageSizeNum;
//
//        // 在把数据传递给mapper端进行sql的操作
//        List<Employee> employees = employeeMapper.pageList(beginIndex,pageSizeNum,employeePageQueryDTO.getName());
//
//        Long total = employeeMapper.count();
//
//        PageResult pageResult = new PageResult(total,employees);
//
//        return pageResult;

        // 老师教的
        // 首先使用pageHelper 来进行存入

        // select * from employee limit 0,10
        // 他会动态的放入进去
        // 开始分页查询

        // 这个的作用就是告诉pageHelper下一条Mapper语句需要分页查询
        //
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        // 这个是总数
        long total = page.getTotal();
        // 这个是获取当前的值
        List<Employee> result = page.getResult();

        return new PageResult(total,result);
    }

    @Override
    public void StartStop(Integer status, Long id) {

        // 首先 为了这个扩张性  可以先在这个里面创建一个实体类
        // 通过这个实体类来进行数据的修改  防止以后需求修改的需求
        Employee build = Employee.builder()
                .id(id)
                .status(status)
                .build();

        // 通过这个可以传入到mapper  动态的处理修改内容
        employeeMapper.update(build);
    }
}
