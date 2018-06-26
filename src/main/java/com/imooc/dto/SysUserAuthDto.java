package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.SysUser;

/**
 * @author 咸鱼
 * @date 2018/6/17 15:53
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserAuthDto extends SysUser {
    /**
     * 父节点，主要是给zTree的简单数据模型使用
     */
    private Integer parentId;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
