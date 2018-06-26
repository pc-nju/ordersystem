package com.imooc.service.impl;

import com.imooc.bean.SalesStatistics;
import com.imooc.dao.SalesStatisticsDao;
import com.imooc.service.SalesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/25 11:02
 */
@Service
public class SalesStatisticsServiceImpl implements SalesStatisticsService {
    @Autowired
    private SalesStatisticsDao salesStatisticsDao;

    @Override
    public List<SalesStatistics> countSalesNumberByHourYesterday(String type) {
        return salesStatisticsDao.countSalesNumberByHourYesterday(type);
    }

    @Override
    public void insertBath(List<SalesStatistics> salesStatisticsList) {
        salesStatisticsDao.insertBatch(salesStatisticsList);
    }

    @Override
    public List<SalesStatistics> getSalesStatisticsYesterday() {
        return salesStatisticsDao.selectSalesStatisticsYesterday();
    }
}
