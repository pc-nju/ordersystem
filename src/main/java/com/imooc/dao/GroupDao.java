package com.imooc.dao;

import com.imooc.bean.Group;
import com.imooc.bean.Menu;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/19 11:18
 */
public interface GroupDao {
    /**
     * 根据条件获取用户组集合
     * @param group 待查询条件对象
     * @return 用户组集合
     */
    List<Group> selectGroups(Group group);

    /**
     * 根据条件获取用户组及其菜单集合
     * @param id 用户组id
     * @return 用户组集合
     */
    Group selectGroupMenusWithId(Integer id);
    /**
     * 根据用户组id获取用户组及其菜单集合详细信息
     * @param groupId 用户组id
     * @return 用户组集合
     */
    Group selectGroupMenusInfoWithId(Integer groupId);

    /**
     * 插入用户组
     * @param group 待插入用户组
     * @return 受影响的行数
     */
    int insertGroup(Group group);

    /**
     * 更新用户组
     * @param group 待更新对象
     * @return 受影响的行数
     */
    int updateGroup(Group group);
    /**
     * 删除用户组
     * @param group 待删除对象
     * @return 受影响的行数
     */
    int deleteGroup(Group group);



}
