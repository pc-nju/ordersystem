package com.imooc.controller;

import com.imooc.constant.PageCodeEnum;
import com.imooc.dto.SysUserDto;
import com.imooc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 潘畅
 * @date 2018/6/13 15:08
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping
    public String login(){
        return "/system/login";
    }
    @RequestMapping("/sessionTimeout")
    public String sessionTimeout(Model model){
        model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.LOGIN_TIMEOUT);
        return "/system/sessionTimeout";
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public String validate(SysUserDto sysUserDto, RedirectAttributes redirectAttributes,
                           HttpServletRequest request, HttpServletResponse response){
        //RedirectAttributes是Spring提供的，专门用于重定向时传送参数（主要是利用Session），一旦传送到目标页面，就会被清除
        if (loginService.validate(sysUserDto, redirectAttributes, request, response)){
            /*
             * 问题：这里为什么使用重定向"redirect:/index"，而直接跳转网址："/system/index"？
             * 原因：是为了防止在进入“index”页面的时候，需要加载数据，直接跳转网址是没有数据的。
             */
            return "redirect:/index";
        }
        return "redirect:/login";
    }
}
