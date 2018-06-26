package com.imooc.bean;

/**
 * @author 咸鱼
 * @date 2018/6/20 14:46
 * 备注：为用户组分配操作使用
 */
public class GroupAction extends BaseBean {
    private Integer groupId;
    private Integer actionId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }
}
