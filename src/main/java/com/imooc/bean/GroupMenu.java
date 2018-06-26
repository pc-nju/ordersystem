package com.imooc.bean;

/**
 * @author 咸鱼
 * @date 2018/6/20 14:45
 * 备注：为用户组分配菜单使用
 */
public class GroupMenu extends BaseBean {
    private Integer groupId;
    private Integer menuId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
