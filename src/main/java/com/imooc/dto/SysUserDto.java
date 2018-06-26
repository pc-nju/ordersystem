package com.imooc.dto;

import com.imooc.bean.SysUser;

/**
 * @author 潘畅
 * @date 2018/6/13 15:29
 */
public class SysUserDto extends SysUser {
    private boolean remember;

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
