package com.imooc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author 咸鱼
 * @date 2018/6/20 14:39
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action extends BaseBean {
    /**
     * 菜单id
     */
    private Integer menuId;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 链接
     */
    private String url;

    /**
     * 动作
     */
    private String method;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
