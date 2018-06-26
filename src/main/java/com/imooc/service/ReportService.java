package com.imooc.service;

import com.imooc.bean.SalesStatistics;
import com.imooc.dto.report.Option;

import java.util.List;
/**
 * @author 咸鱼
 * @date 2018/6/25 19:22
 */
public interface ReportService {
    /**
     * 根据统计数据，构建echarts设置参数option
     * @param salesStatisticsList 统计数据列表
     * @return Option
     */
    Option constructOption(List<SalesStatistics> salesStatisticsList);
}
