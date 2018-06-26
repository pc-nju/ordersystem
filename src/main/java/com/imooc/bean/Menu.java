package com.imooc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/20 7:37
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu extends BaseBean {
    /**
     * 菜单名称
     */
    private String name;
    private String url;
    private Integer parentId;
    /**
     * 排序权重
     */
    private Integer orderNum;
    /**
     * 菜单下的操作集合
     */
    private List<Action> actionList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }
}
