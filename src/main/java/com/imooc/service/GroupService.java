package com.imooc.service;

import com.imooc.bean.Group;
import com.imooc.dto.GroupDto;
import com.imooc.dto.ResultDto;

/**
 * @author 咸鱼
 * @date 2018/6/19 11:17
 */
public interface GroupService {
    /**
     * 获取所有的用户组
     * @return 用户组集合及状态码
     */
    ResultDto getAllGroups();
    /**
     * 根据id获取指定用户组
     * @param id 用户组id
     * @return 用户组及状态码
     */
    ResultDto getGroupWithId(Integer id);

    /**
     * 根据id获取指定用户组
     * @param id 用户组id
     * @return 用户组及状态码
     */
    ResultDto getGroupMenusWithId(Integer id);

    /**
     * 增加用户组
     * @param group 待增加用户组
     * @return 状态码
     */
    ResultDto addGroup(Group group);

    /**
     * 修改用户组
     * @param group 待修改用户组
     * @return 状态码
     */
    ResultDto modifyGroup(Group group);

    /**
     * 删除用户组
     * @param group 待删除用户组
     * @return 状态码
     */
    ResultDto removeGroup(Group group);

    /**
     * 为用户组分配菜单
     * @param groupDto 传过来的参数
     * @return 状态码
     */
    ResultDto assignMenusForGroup(GroupDto groupDto);
}
