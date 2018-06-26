package com.imooc.dao;

import com.imooc.bean.SalesStatistics;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/25 11:03
 */
public interface SalesStatisticsDao {
    /**
     * 按商品类别和时间段统计商品销量
     * @param type 统计的类别（是按照商品的类比，还是城市进行统计）
     * @return 统计数据集合
     */
    List<SalesStatistics> countSalesNumberByHourYesterday(String type);

    /**
     * 插入统计数据进统计表
     * @param salesStatisticsList 统计数据集合
     */
    void insertBatch(List<SalesStatistics> salesStatisticsList);

    /**
     * 获取昨天的统计数据
     * @return 统计数据集合
     */
    List<SalesStatistics> selectSalesStatisticsYesterday();
}
