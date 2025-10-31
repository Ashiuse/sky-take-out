package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 统计报表
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "统计报表相关接口")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 统计营业额数据
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("统计营业额数据")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                                       LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                       LocalDate end) {
        return Result.success(reportService.getTurnover(begin, end));
    }

    /**
     * 统计用户数据
     *
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("统计用户数据")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate end) {
        return Result.success(reportService.getUserStatistics(begin, end));
    }


}
