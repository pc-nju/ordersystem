package com.imooc.service.impl;

import com.imooc.bean.SysUser;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dao.GroupActionDao;
import com.imooc.dao.SysUserDao;
import com.imooc.dao.GroupMenuDao;
import com.imooc.dto.PageCodeDto;
import com.imooc.dto.SysUserAuthDto;
import com.imooc.service.SysUserService;
import com.imooc.util.Md5Util;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/17 15:17
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;


    @Override
    public boolean addUser(SysUser sysUser) {
        //新增用户默认密码为用户名
        sysUser.setPassword(Md5Util.getMD5(sysUser.getUsername()));
        return sysUserDao.insertUser(sysUser) == 1;
    }

    @Override
    public PageCodeDto deleteSysUser(Integer id) {
        SysUser sysUser = sysUserDao.selectUserWithId(id);
        if (sysUser == null){
            return new PageCodeDto(PageCodeEnum.USER_NOT_EXISTS);
        }
        int affectedRow = sysUserDao.deleteSysUserWithId(id);
        if (affectedRow != 1){
            return new PageCodeDto(PageCodeEnum.DELETE_FAILURE);
        }
        return new PageCodeDto(PageCodeEnum.DELETE_SUCCESS);
    }



    @Override
    public boolean modifyUser(SysUser sysUser) {
        return sysUserDao.modifyUser(sysUser) == 1;
    }

    @Override
    public List<SysUserAuthDto> getAllUsers() {
        SysUser sysUser = new SysUser();
        return wrapSysUserList(sysUserDao.selectUser(sysUser));
    }

    @Override
    public SysUserAuthDto getUserWithId(Integer id) {
        return wrapSysUser(sysUserDao.selectUserWithId(id));
    }

    @Override
    public SysUser getSysUserById(Integer id) {
        return getSysUser(id);
    }

    private SysUser getSysUser(Integer id){
        return sysUserDao.selectUserWithId(id);
    }
    private List<SysUser> getSysUserList(SysUser sysUser){
        return sysUserDao.selectUser(sysUser);
    }


    private List<SysUserAuthDto> wrapSysUserList(List<SysUser> sysUserList) {
        List<SysUserAuthDto> newSysUserList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sysUserList)){
            for (SysUser sysUser : sysUserList){
                newSysUserList.add(wrapSysUser(sysUser));
            }
        }
        return newSysUserList;
    }

    private SysUserAuthDto wrapSysUser(SysUser sysUser) {
        SysUserAuthDto sysUserAuthDto = new SysUserAuthDto();
        if (sysUser != null){
            sysUserAuthDto.setId(sysUser.getId());
            sysUserAuthDto.setParentId(0);
            sysUserAuthDto.setGroupId(sysUser.getGroupId());
            sysUserAuthDto.setNickName(sysUser.getNickName());
            sysUserAuthDto.setUsername(sysUser.getUsername());
        }
        return sysUserAuthDto;
    }
}
