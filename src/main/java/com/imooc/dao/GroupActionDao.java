package com.imooc.dao;

import com.imooc.bean.GroupAction;
import org.apache.ibatis.annotations.Param;

/**
 * @author 咸鱼
 * @date 2018/6/21 11:57
 */
public interface GroupActionDao {

    /**
     * 批量插入group与action的映射关系
     * @param groupId 用户组id
     * @param actionIds action的id数组
     * @return 受影响的行数
     */
    int insertGroupActionBatch(@Param("groupId") Integer groupId, @Param("actionIds")Integer[] actionIds);
    /**
     * 删除“用户组/菜单（Menu）”关联关系
     * @param groupAction 待删除条件
     */
    void deleteGroupAction(GroupAction groupAction);

    /**
     * 根据菜单id，删除该菜单下的所有Action与用户的关联关系“用户组/操作（Action）”
     * @param menuId 菜单id
     */
    void deleteGroupActionByMenuId(Integer menuId);
}
