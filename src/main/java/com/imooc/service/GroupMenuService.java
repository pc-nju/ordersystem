package com.imooc.service;

import com.imooc.bean.GroupMenu;

/**
 * @author 咸鱼
 * @date 2018/6/22 14:31
 */
public interface GroupMenuService {
    /**
     * 删除指定groupMenu
     * @param groupMenu 待删除对象
     */
    void deleteGroupMenu(GroupMenu groupMenu);

    /**
     * 根据groupId删除指定groupMenu
     * @param groupId groupId
     */
    void deleteGroupMenuByGroupId(Integer groupId);
    /**
     * 批量插入group与menu的映射关系
     * @param groupId 用户组id
     * @param menuIds menu的id数组
     * @return 受影响的行数
     */
    int insertGroupMenuBath(Integer groupId, Integer[] menuIds);

    /**
     * 根据menuId删除指定groupMenu
     * @param menuId menuId
     */
    void deleteGroupMenuByMenuId(Integer menuId);

}
