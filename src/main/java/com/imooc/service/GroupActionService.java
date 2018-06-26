package com.imooc.service;

import com.imooc.bean.GroupAction;

/**
 * @author 咸鱼
 * @date 2018/6/22 14:32
 */
public interface GroupActionService {
    /**
     * 删除指定groupAction
     * @param groupAction 待删除对象
     */
    void deleteGroupAction(GroupAction groupAction);
    /**
     * 根据用户组id，删除与该用户组相关联的“用户组/操作（Action）”关联关系
     * @param groupId 用户组id
     */
    void deleteGroupActionByGroupId(Integer groupId);

    /**
     * 批量插入group与action的映射关系
     * @param groupId 用户组id
     * @param actionIds action的id数组
     * @return 受影响的行数
     */
    int insertGroupActionBatch(Integer groupId, Integer[] actionIds);

    /**
     * 根据菜单id，删除该菜单下的所有Action与用户的关联关系“用户组/操作（Action）”
     * @param menuId 菜单id
     */
    void deleteGroupActionByMenuId(Integer menuId);
    /**
     * 根据Action的id，删除与该用户组相关联的“用户组/操作（Action）”关联关系
     * @param actionId Action的id
     */
    void deleteGroupActionByActionId(Integer actionId);
}
