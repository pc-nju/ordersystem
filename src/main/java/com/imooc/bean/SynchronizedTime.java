package com.imooc.bean;


import java.util.Date;

/**
 * @author **
 * @date 2018/6/10 20:44
 */
public class SynchronizedTime extends BaseBean {
    /**
     * 同步的数据类型，我这里写成常量
     */
    private String type;
    /**
     * 最后一次的同步时间
     */
    private Date updateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
