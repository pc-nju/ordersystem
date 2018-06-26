package com.imooc.constant;

/**
 * @author 潘畅
 * @date 2018/6/14 11:26
 */
public interface SessionKeyConst {
    String SESSION_USER = "user";
    String SESSION_LOGIN_TOKEN = "login_token";
    /**
     * 登陆过期时间
     */
    Integer EXPIRE_TIME = 7*24*60*60;
    /**
     * 用户名、密码MD5加密盐值（大白话，就是秘钥）
     */
    String ENCRYPT_SALT= "mmp";
    String SESSION_MENU_INFO  = "menu_info";
    String SESSION_ACTION_INFO  = "action_info";
}
