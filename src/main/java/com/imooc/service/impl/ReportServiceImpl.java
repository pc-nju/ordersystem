package com.imooc.service.impl;

import com.imooc.bean.SalesStatistics;
import com.imooc.dto.report.Legend;
import com.imooc.dto.report.Option;
import com.imooc.dto.report.Serie;
import com.imooc.dto.report.XAxis;
import com.imooc.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 咸鱼
 * @date 2018/6/25 19:23
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public Option constructOption(List<SalesStatistics> salesStatisticsList) {
        /*
         * Set<String>：保证类别唯一（不会重复）
         * TreeSet：保证每次取出来的顺序是一样的（HashSet每次取出来的顺序都可能不一样）
         */
        Set<String> nameSet = new TreeSet<>();
        /*
         * 分析：因为同一类别不同时间段的数据需要组成一个集合，所以我们需要构建（“类别+时间”，数值）这样一个键值对，
         *       这样我们在构建整个数据集的时候，就可以通过“类别+时间”来确定有没有值，没有的用“0”补上
         */
        HashMap<String, String> datas = new HashMap<>();

        for (SalesStatistics salesStatistics : salesStatisticsList){
            String name = salesStatistics.getBusinessCategory();
            String hour = salesStatistics.getHour();
            Integer value = salesStatistics.getSalesNum();
            //构建类别名数据集
            nameSet.add(name);

            //构建（“类别+时间”，数值）Map集
            datas.put(name+hour, String.valueOf(value));
        }
        Option option = new Option();
        Legend legend = new Legend();
        XAxis xAxis = new XAxis();
        List<Serie> series = new ArrayList<>();

        //构造legend属性
        List<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);
        legend.setData(nameList);
        option.setLegend(legend);

        //构造xAxis属性
        List<String> hours = new ArrayList<>();
        for (int i=0; i<=23; i++){
            //格式化数据，不足两位，用0补齐
            hours.add(String.format("%02d", i));
        }
        xAxis.setData(hours);
        option.setxAxis(xAxis);

        //构造series属性

        //要求：同一个类别的组成一组数据，数据为空的，用0补齐

        /*
         * 问题：为什么先遍历name，再遍历hour?
         * 原因：这样就保证name相同的数值，能够合并到一个Serie对象中
         */
        for (String name : nameSet){
            Serie serie = new Serie();
            List<String> values = new ArrayList<>();
            for (String hour : hours){
                serie.setName(name);
                serie.setStack("总量");
                serie.setType("line");
                values.add(datas.get(name + hour) == null ? "0" : datas.get(name + hour));
            }
            serie.setData(values);
            series.add(serie);
        }
        option.setSeries(series);
        return option;
    }
}
