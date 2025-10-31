package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据时间区间查询营业额
     *
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
        //创建集合存放范围内每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        //开始存放每一天的日期
        dateList.add(begin);
        while (!begin.equals(end)) {
            //日期加1
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);//这天的00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);//这天的23:59:59

            // 查询date日期的营业额数据
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);//订单状态为已完成
            map.put("begin", beginTime);
            map.put("end", endTime);
            Double turnover = orderMapper.sumByMap(map);

            // 营业额数据保存在turnoverList
            turnover = (turnover == null ? 0.0 : turnover);
            turnoverList.add(turnover);
        }
        // 封装返回结果(将日期和对应的营业额转换为字符串)
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

}
