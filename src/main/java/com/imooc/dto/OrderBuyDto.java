package com.imooc.dto;

/**
 * @author 潘畅
 * @date 2018/6/10 10:40
 * 用来承接用户买单传递过来的参数
 */
public class OrderBuyDto {
    /**
     * 商户id
     */
    private Integer id;
    /**
     * 手机号
     */
    private String username;
    /**
     * 消费人数
     */
    private Integer num;
    /**
     * 购买价格
     */
    private Double price;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
