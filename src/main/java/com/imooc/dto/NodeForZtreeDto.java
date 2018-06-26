package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.BaseBean;

/**
 * @author 咸鱼
 * @date 2018/6/20 18:32
 * "NodeForZtreeDto"用于菜单树展示的节点：
 *     菜单树需要展示两种节点：菜单（Menu）、动作（Action），这里面两种节点全部抽象成一种节点"NodeForZtreeDto"。
 * 这样有一个问题，那就是主键（id）由于来自两张表，就会存在主键重复的问题，若把这样的数据返回给“ztree”，
 * “ztree”利用主键在搭建树的过程中，会出现展示混乱，所以这里面需要所有涉及到主键的地方
 * （sys_menu:id,parentId  |||  sys_action:id,menuId）加上前缀：
 *     来自菜单表的节点：菜单前缀+菜单表ID
 *     来自动作表的节点：动作前缀+动作表ID
 * 所以需要两个字段：comboId（混合id）和comboParentId（混合父Id）
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeForZtreeDto extends BaseBean {
    private Integer id;
    private String name;
    /**
     *     在菜单树上我们需要同时展示2种节点：菜单、动作
     *     这来自两张表，带来的问题就是主键（id）会重复，这样会造成“ztree”展示混乱，为了区分这两种节点，
     * 将在这两种节点的原主键上加前缀.
     *     来自菜单表的节点：菜单前缀+菜单表ID
     *     来自动作表的节点：动作前缀+动作表ID
     */
    private String comboId;
    /**
     * 同上
     */
    private String comboParentId;
    /**
     * 是否展开父节点
     */
    private boolean open;
    /**
     * 是否是父节点
     */
    private String isParent;

    /**
     * 菜单表节点前缀
     */
    public static final String PREFIX_MENU = "MENU_";
    /**
     * 动作表节点前缀
     */
    public static final String PREFIX_ACTION = "ACTION_";

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }

    public String getComboParentId() {
        return comboParentId;
    }

    public void setComboParentId(String comboParentId) {
        this.comboParentId = comboParentId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }
}
