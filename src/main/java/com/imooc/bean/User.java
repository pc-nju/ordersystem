package com.imooc.bean;

import java.util.Date;

/**
 * @author 潘畅
 * @date 2018/6/8 10:18
 */
public class User extends BaseBean {
    private Long phone;
    private String name;
    private String password;
    private Date createDate;

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
