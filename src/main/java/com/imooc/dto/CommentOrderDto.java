package com.imooc.dto;

/**
 * @author 潘畅
 * @date 2018/6/11 11:45
 * 这是从前端接收评论参数的
 */
public class CommentOrderDto {
    /**
     * 订单id
     */
    private Integer id;
    /**
     * 手机号
     */
    private String username;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 星级
     */
    private Integer star;
    /**
     * 登录token
     */
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
