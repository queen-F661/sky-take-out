package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 营业额统计
     * */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计接口
     *
     * @return
     *
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);


    /**
     * 订单统计接口
     * */
    OrderReportVO orderReport(LocalDate begin, LocalDate end);
}
