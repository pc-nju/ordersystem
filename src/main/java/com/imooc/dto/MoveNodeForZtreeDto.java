package com.imooc.dto;

import com.imooc.bean.BaseBean;

/**
 * @author 咸鱼
 * @date 2018/6/23 22:00
 */
public class MoveNodeForZtreeDto extends BaseBean {
    /**
     * 拖动节点id
     */
    private Integer dropNodeId;
    /**
     * 目标节点id
     */
    private Integer targetNodeId;
    /**
     * 拖动节点的类型：“menu”菜单节点  “action”Action节点
     */
    private String dropNodeType;

    public Integer getDropNodeId() {
        return dropNodeId;
    }

    public void setDropNodeId(Integer dropNodeId) {
        this.dropNodeId = dropNodeId;
    }

    public Integer getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(Integer targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public String getDropNodeType() {
        return dropNodeType;
    }

    public void setDropNodeType(String dropNodeType) {
        this.dropNodeType = dropNodeType;
    }
}
