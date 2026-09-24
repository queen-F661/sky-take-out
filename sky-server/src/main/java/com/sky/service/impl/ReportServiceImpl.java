package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 计算当前的时间
     * 从当前的开始到结束 都放到一个集合当中
     * */
    public List<LocalDate> beginTime(LocalDate begin,LocalDate end){
        // 首先 你要确定当前订单接口的日期
        // 这个日期是以字符串形式存在的 以逗号进行间隔
        // 可以根据当前的list存储到一个集合
        // 在从这个集合中转换成一个字符串

        // 已知道这个当前第一个值为begin 最后一个值
        ArrayList<LocalDate> localDates = new ArrayList<>();

        localDates.add(begin);

        // 因为最后一个值为end,那么可以使用while循环进行判断当前传入的值为不为最后一个值
        // 如果是最后一个值 那么就可以直接跳过循环
        while(!begin.equals(end)){
            // 这个是添加一天
            // 在把当前的值传递给begin
            begin = begin.plusDays(1);
            // 在根据当前的begin来进行数据的传递 一直到最后
            localDates.add(begin);
        }
        return localDates;
    }

    /**
     * 营业额统计
     * */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> localDates = beginTime(begin, end);
        // 因为你最后一个值没有传递 所以要进行补全 所以要加上最后一个值
        // localDates.add(end);

        // 因为你知道当前的订单表是根据日期来进行计算
        // 这样就可以知道知道要把当前的值进行循环出来 循环出来 在根据每一天来进行计算数据

        ArrayList<Double> integers = new ArrayList<>();
        for (LocalDate localDate : localDates) {

            // 因为这个营业额 是要在当前的状态为已完成的状态
            // 如果是其他 不算完成 也就是不算营业额
            Integer completed = Orders.COMPLETED;

            // 因为我们知道当前LocalData只能传递年月日
            // 时间秒是没有传递的 所以当前的end和beigin要进行传递数据
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);


            // 在根据这些值进行写sql语句来进行传递
            // 在把这些值传递给map里面 在使用map一次性传递
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("begin",beginTime);
            objectObjectHashMap.put("end",endTime);
            objectObjectHashMap.put("status",completed);

            // 在把值进行数据的传递
            Double sum = orderMapper.sum(objectObjectHashMap);
            // 如果没有值的话 那他就会传递一个空值
            // 如果传递空职就不合理 要把空转成0
            sum = sum == null ? 0.0 : sum;
            integers.add(sum);
        }

        // 在把当前的营业额变成string类型 和 当前的时间也是
        String dateList = StringUtils.join(localDates,',');
        String turnoverList = StringUtils.join(integers,',');

        return TurnoverReportVO.builder()
                        .dateList(dateList)
                        .turnoverList(turnoverList)
                        .build();
    }

    /**
     * 用户统计接口
     *
     * @return
     *
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> localDates = beginTime(begin, end);

        // 用户总量
        // **用户总量 (totalUserList)**：**截止到这一天，系统里所有注册过的用户累计总数**
        // 1.是根据我们传递当前的数据进行计算  应该是属于是指示剂整涨

        // 创建个二个arrylist
        ArrayList<Integer> historyUser = new ArrayList<>();
        ArrayList<Integer> NewUser = new ArrayList<>();

        // 从时间下手 每次添加一天 给一个值 第一天是计算当前的 第二次是计算二天的
        for (LocalDate localDate : localDates) {
            // 因为你是计算历史到你今天的数据
            // 所以我们就可以使用
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            //在根据这个数据来进行找当前的用户总量
            Integer history = userMapper.historyUser(null,endTime);

            history = history == null ? 0 : history;

            // 这个是获取新的数据
            Integer newUser = userMapper.historyUser(beginTime,endTime);

            newUser = newUser == null ? 0 : newUser;

            historyUser.add(history);
            NewUser.add(newUser);
        }

        // 在把数据转换
        return UserReportVO.builder()
                .dateList(StringUtils.join(localDates,','))
                .totalUserList(StringUtils.join(historyUser,','))
                .newUserList(StringUtils.join(NewUser,','))
                .build();
    }

    /**
     * 订单统计接口
     * */
    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {

        // 从开始到结束的时间
        List<LocalDate> localDates = beginTime(begin, end);

        // 每日订单数
        ArrayList<Integer> orderCountList = new ArrayList<>();
        // 每日有效订单数
        ArrayList<Integer> validOrderCountList = new ArrayList<>();

        Integer status = Orders.COMPLETED;
        for (LocalDate localDate : localDates) {
            //每日订单数，以逗号分隔，例如：260,210,215
            //每日有效订单数，以逗号分隔，例如：20,21,10

            // 这里面的区别就是当前的值status为不为5
            // 在时间都是一样的 需要进行出来
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);


            // 如果说是有5就是当前每日有效订单数
            Integer history = orderMapper.countValidOrder(beginTime,endTime,status);
            // 如果说是没有5的情况下就是每日订单数
            Integer Count = orderMapper.countValidOrder(beginTime,endTime,null);



            orderCountList.add(Count);
            validOrderCountList.add(history);
        }

        // 循环list 把当前时间内所有的数据累加就行
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer valiOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        // 订单完成率
        Double orderCompletionRate = 0.0;
        // 判断分母为不为0
        // 如果为0 那么数据就会报错  所以要判断一下
        if(totalOrderCount != 0){
            orderCompletionRate = (double)valiOrderCount / totalOrderCount;
        }


        OrderReportVO build = OrderReportVO.builder()
                .dateList(StringUtils.join(localDates, ','))
                .orderCountList(StringUtils.join(orderCountList, ','))
                .validOrderCountList(StringUtils.join(validOrderCountList, ','))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(valiOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();

        return build;
    }

    /**
     * 查询销量排名top10接口
     * */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {

        // 时间要转换一下
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOS = orderMapper.top10(beginTime,endTime);

        // 在把当前的list进行数据的拆解
        List<String> names = goodsSalesDTOS.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names,',');

        List<Integer> numbers = goodsSalesDTOS.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers,',');
        return SalesTop10ReportVO.builder().nameList(nameList).numberList(numberList).build();
    }

}
