package com.imooc.dao;

import com.imooc.bean.SysUser;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/13 17:02
 */
public interface SysUserDao {
    /**
     * 查询用户
     * @param sysUser 待查询条件
     * @return 用户集合
     */
    List<SysUser> selectUser(SysUser sysUser);

    /**
     * 根据id查询系统用户
     * @param id 用户id
     * @return 用户
     */
    SysUser selectUserWithId(Integer id);

    /**
     * 根据用户id，删除用户
     * @param id 用户id
     * @return 删除结果
     */
    int deleteSysUserWithId(Integer id);

    /**
     * 新增用户
     * @param sysUser 待新增对象
     * @return 受影响的行数
     */
    int insertUser(SysUser sysUser);

    /**
     * 更新用户信息
     * @param sysUser 待更新对象
     * @return 受影响的行数
     */
    int modifyUser(SysUser sysUser);

    /**
     * 置空用户的groupId
     * @param sysUser 置空条件
     */
    void updateGroupIdNull(SysUser sysUser);
}
