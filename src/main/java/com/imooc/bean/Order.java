package com.imooc.bean;


import java.util.Date;

/**
 * @author 潘畅
 * @date 2018/6/10 14:58
 */
public class Order extends BaseBean {
    /**
     * 商户id
     */
    private Integer businessId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 已售数量
     */
    private Integer num;
    /**
     * 评论状态
     * 0：未评论
     * 2：已评论
     */
    private Integer commentState;
    /**
     * 价格
     */
    private Double price;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 用户（多对1）
     */
    private User user;
    /**
     * 评论（多对1）
     */
    private Business business;

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCommentState() {
        return commentState;
    }

    public void setCommentState(Integer commentState) {
        this.commentState = commentState;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
