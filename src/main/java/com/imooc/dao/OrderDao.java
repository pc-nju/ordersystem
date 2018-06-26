package com.imooc.dao;

import com.imooc.bean.Order;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/10 14:57
 */
public interface OrderDao {
    /**
     * 插入订单
     * @param order 待插入订单
     * @return 受影响的条数
     */
    int insertOrder(Order order);

    /**
     * 分页查询订单
     * @param order 待查询对象
     * @return 订单列表
     */
    List<Order> searchOrderListByPage(Order order);

    /**
     * 根据订单id获取订单
     * @param id 订单id
     * @return 订单
     */
    Order selectOrderById(Integer id);

    /**
     * 更新订单
     * @param order 待更新对象
     * @return 受影响的条数
     */
    int updateOrder(Order order);
}
