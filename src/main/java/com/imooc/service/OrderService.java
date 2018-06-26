package com.imooc.service;

import com.imooc.bean.SalesStatistics;
import com.imooc.dto.ApiCodeDto;
import com.imooc.dto.OrderBuyDto;
import com.imooc.dto.OrderDto;
import com.imooc.dto.OrderListDto;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/10 14:56
 */
public interface OrderService {
    /**
     * 下订单
     * @param orderBuyDto 订单参数
     * @return 下订单状态
     */
    ApiCodeDto order(OrderBuyDto orderBuyDto);

    /**
     * 根据手机号获取订单列表
     * @param phone 手机号
     * @return 订单列表
     */
    List<OrderDto> getOrderListByPhoneForApi(String phone);

    /**
     * 根据手机号获取订单列表
     * @param phone 手机号
     * @return 订单列表
     */
    OrderListDto getOrderListByPhoneForApiByTest(String phone);

    /**
     * 获取订单列表
     * @param model 数据模型
     */
    void getOrderList(Model model);

    /**
     * 根据条件搜索订单
     * @param searchKey 搜索关键词
     * @param currentPage 当前页
     * @param model 数据模型
     */
    void search(String searchKey, Integer currentPage, Model model);
}
