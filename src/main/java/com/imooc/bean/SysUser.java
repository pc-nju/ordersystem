package com.imooc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * @author 潘畅
 * @date 2018/6/13 14:49
 */
public class SysUser extends BaseBean {
    private Integer groupId;
    private String username;
    private String password;
    private String nickName;
    private Date updateTime;
    private Date createTime;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
