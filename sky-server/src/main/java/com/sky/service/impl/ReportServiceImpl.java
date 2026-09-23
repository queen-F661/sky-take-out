package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 营业额统计
     * */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {

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
}
