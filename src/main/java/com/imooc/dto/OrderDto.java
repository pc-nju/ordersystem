package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author 潘畅
 * @date 2018/6/10 16:47
 * 这个只为API使用，因为API把id字段设置成手机号了，和我们后台冲突了
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto implements Serializable{
    /**
     * 手机号(for api)/订单号（for jsp）
     */
    private Long id;
    /**
     * 手机号（for jsp）
     */
    private Long phone;
    /**
     * 商户图片
     */
    private String img;
    /**
     * 商户标题
     */
    private String title;
    /**
     * 购买数量
     */
    private Integer count;
    /**
     * 价格
     */
    private Double price;
    /**
     * 评论状态 0：未评论 2：已评论
     */
    private Integer commentState;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
