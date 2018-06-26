package com.imooc.dto;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/11 14:52
 */
public class OrderListDto {
    private boolean hasMore;
    private List<OrderDto> orderDtoList;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<OrderDto> getOrderDtoList() {
        return orderDtoList;
    }

    public void setOrderDtoList(List<OrderDto> orderDtoList) {
        this.orderDtoList = orderDtoList;
    }
}
