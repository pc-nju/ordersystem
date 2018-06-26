package com.imooc.service;

import com.imooc.bean.SysUser;
import com.imooc.dto.PageCodeDto;
import com.imooc.dto.ResultDto;
import com.imooc.dto.SysUserAuthDto;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/17 15:17
 */
public interface SysUserService {
    /**
     * 增加用户
     * @param sysUser 待增加对象
     * @return true:成功 false：失败
     */
    boolean addUser(SysUser sysUser);

    /**
     * 根据id，删除用户
     * @param id 用户id
     * @return 删除结果
     */
    PageCodeDto deleteSysUser(Integer id);

    /**
     * 修改用户信息
     * @param sysUser 待修改条件
     * @return true:修改成功 false:修改失败
     */
    boolean modifyUser(SysUser sysUser);

    /**
     * 获取所有的后台用户
     * @return 后台用户列表
     */
    List<SysUserAuthDto> getAllUsers();

    /**
     * 根据用户ID选择用户
     * @param id 用户ID
     * @return 用户
     */
    SysUserAuthDto getUserWithId(Integer id);
    /**
     * 根据用户ID获取用户
     * @param id 用户ID
     * @return 用户
     */
    SysUser getSysUserById(Integer id);


}
