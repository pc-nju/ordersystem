package com.imooc.dao;

import com.imooc.bean.GroupMenu;
import org.apache.ibatis.annotations.Param;

/**
 * @author 咸鱼
 * @date 2018/6/21 11:57
 */
public interface GroupMenuDao {

    /**
     * 批量插入group与menu的映射关系
     * @param groupId 用户组id
     * @param menuIds menu的id数组
     * @return 受影响的行数
     */
    int insertGroupMenuBath(@Param("groupId") Integer groupId, @Param("menuIds") Integer[] menuIds);

    /**
     * 删除“用户组/菜单（Menu）”关联关系
     * @param groupMenu 待删除条件
     */
    void deleteGroupMenu(GroupMenu groupMenu);
}
