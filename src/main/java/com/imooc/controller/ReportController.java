package com.imooc.controller;

import com.imooc.bean.SalesStatistics;
import com.imooc.constant.DictionaryConstant;
import com.imooc.dto.ResultDto;
import com.imooc.dto.report.Option;
import com.imooc.service.ReportService;
import com.imooc.service.SalesStatisticsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/25 15:53
 */
@RequestMapping("/report")
@Controller
public class ReportController {
    @Autowired
    private SalesStatisticsService salesStatisticsService;
    @Autowired
    private ReportService reportService;

    @RequestMapping
    public String initReport(){
        return "/report/orderCount";
    }

    @ResponseBody
    @RequestMapping("/saleNumber")
    public ResultDto getSaleNumber(){
        List<SalesStatistics> salesStatisticsList = salesStatisticsService.getSalesStatisticsYesterday();
        if (CollectionUtils.isEmpty(salesStatisticsList)){
            return new ResultDto().failure();
        }
        Option option = reportService.constructOption(salesStatisticsList);
        return new ResultDto().success().setOption(option);
    }
}
