package com.imooc.bean;

import java.util.Date;

/**
 * @author 咸鱼
 * @date 2018/6/25 10:44
 */
public class SalesStatistics extends BaseBean {
    /**
     * 就某对象分类统计的类名，比如
     * 商品就分为：美食、景点、旅游。。。
     */
    private String businessCategory;
    /**
     * 分时间段统计，主要由00-23点
     */
    private String hour;
    /**
     * 销售数量
     */
    private Integer salesNum;
    /**
     * 类别，在这个类中，默认都为“business”
     */
    private String type;
    private Date createTime;

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Integer salesNum) {
        this.salesNum = salesNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
