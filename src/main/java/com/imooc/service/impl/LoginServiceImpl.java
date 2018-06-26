package com.imooc.service.impl;

import com.imooc.bean.SysUser;
import com.imooc.constant.SessionKeyConst;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dao.SysUserDao;
import com.imooc.dto.SysUserDto;
import com.imooc.service.LoginService;
import com.imooc.util.Md5Util;
import com.imooc.util.VerifyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/13 17:01
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserDao sysUserDao;
    @Override
    public boolean validate(SysUserDto sysUserDto, RedirectAttributes redirectAttributes
            , HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtil.validateUsername(sysUserDto.getUsername())){
            redirectAttributes.addFlashAttribute(PageCodeEnum.KEY, PageCodeEnum.USERNAME_ERROR);
            return false;
        }
        if (!VerifyUtil.validatePassword(sysUserDto.getPassword())){
            redirectAttributes.addFlashAttribute(PageCodeEnum.KEY, PageCodeEnum.PASSWORD_ERROR);
            return false;
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, sysUser);
        List<SysUser> sysUserList = sysUserDao.selectUser(sysUser);
        if (sysUserList.size() != 1){
            redirectAttributes.addFlashAttribute(PageCodeEnum.KEY, PageCodeEnum.LOGIN_FAILURE);
            return false;
        }
        redirectAttributes.addFlashAttribute(PageCodeEnum.KEY, PageCodeEnum.LOGIN_SUCCESS);
        //将用户设置到Session中（留服务器内部判断是否登录使用！）
        request.getSession().setAttribute(SessionKeyConst.SESSION_USER, sysUserList.get(0));

        //将Session设置到Cookie中（留服务器判断浏览器是否登录使用！）
        if (sysUserDto.isRemember()){
            SysUser user = sysUserList.get(0);
            String loginToken = generateLoginToken(user.getUsername(), user.getPassword(), user.getId());
            Cookie cookie = new Cookie(SessionKeyConst.SESSION_LOGIN_TOKEN, loginToken);
            //若我们这里不设置path，则只要访问“/login”时才会带该cooke
            cookie.setPath("/");
            cookie.setMaxAge(SessionKeyConst.EXPIRE_TIME);
            response.addCookie(cookie);
        }
        return true;
    }

    /**
     * 根据用户名、密码和用户id生成登录令牌
     * @param username 用户名
     * @param password 密码
     * @param userId 用户id
     * @return 登录令牌
     */
    public static String generateLoginToken(String username, String password, Integer userId) {
        return Md5Util.getMD5WithSalt(username+password, SessionKeyConst.ENCRYPT_SALT) + ":" + userId;
    }
}
