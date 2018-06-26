package com.imooc.bean;

import java.util.Date;

/**
 * @author 潘畅
 * @date 2018/6/11 11:39
 */
public class Comment extends BaseBean {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 星级
     */
    private Integer star;
    /**
     * 评论创建时间
     */
    private Date createTime;
    /**
     * 用户
     */
    private Order order;
    /**
     * 用户（这里是为了查询评论提供用户手机号入口，本质上是想共用一个查询条件）
     */
    private User user;
    /**
     * 商户（这里是为了查询评论提供商户id入口，本质上是想共用一个查询条件）
     */
    private Business business;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
