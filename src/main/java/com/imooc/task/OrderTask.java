package com.imooc.task;

import com.imooc.bean.SalesStatistics;
import com.imooc.constant.DictionaryConstant;
import com.imooc.service.OrderService;
import com.imooc.service.SalesStatisticsService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/25 10:48
 */
@Component("orderTask")
public class OrderTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTask.class);
    @Autowired
    private SalesStatisticsService salesStatisticsService;
    /**
     * 定时按商品类别统计每小时的销量
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void synchronizedSaleNumberByBusinessCategory(){
        LOGGER.info("按商品类别统计每小时的销量开始同步");
        //只统计前一天的销量
        List<SalesStatistics> salesStatisticsList = salesStatisticsService.countSalesNumberByHourYesterday(
                DictionaryConstant.TYPE_DICTIONARY_CATEGORY);
        if (CollectionUtils.isNotEmpty(salesStatisticsList)){
            for (SalesStatistics salesStatistics : salesStatisticsList){
                salesStatistics.setType(DictionaryConstant.TYPE_DICTIONARY_CATEGORY);
            }
            //将统计得到的数据插入到统计表中
            salesStatisticsService.insertBath(salesStatisticsList);
        }
        LOGGER.info("按商品类别统计每小时的销量同步结束");
    }
}
