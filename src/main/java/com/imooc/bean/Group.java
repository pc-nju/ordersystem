package com.imooc.bean;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/18 17:45
 */
public class Group extends BaseBean {
    /**
     * 用户组名称
     */
    private String name;
    /**
     * 一对多关系映射（Group和Menu并不直接产生联系，而是靠中间表产生联系）
     */
    private List<Menu> menuList;
    /**
     * 一对多关系映射（Group和Menu并不直接产生联系，而是靠中间表产生联系）
     */
    private List<Action> actionList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }
}
