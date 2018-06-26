package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.Group;

/**
 * @author 咸鱼
 * @date 2018/6/18 17:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto extends Group {
    private Integer parentId;

    private Integer[] menuIdList;
    private Integer[] actionIdList;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer[] getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(Integer[] menuIdList) {
        this.menuIdList = menuIdList;
    }

    public Integer[] getActionIdList() {
        return actionIdList;
    }

    public void setActionIdList(Integer[] actionIdList) {
        this.actionIdList = actionIdList;
    }
}
