package com.imooc.service;

import com.imooc.dto.SysUserDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 潘畅
 * @date 2018/6/13 17:01
 */
public interface LoginService {
    /**
     * 登录校验
     * @param sysUserDto 待校验用户
     * @param redirectAttributes 重定向容器
     * @param request 请求对象
     * @param response 响应对象
     * @return true 登陆成功 false 登录失败
     */
    boolean validate(SysUserDto sysUserDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response);
}
