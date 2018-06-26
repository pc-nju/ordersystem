package com.imooc.controller;

import com.imooc.bean.SysUser;
import com.imooc.constant.PageCodeEnum;
import com.imooc.constant.SessionKeyConst;
import com.imooc.dto.ResultDto;
import com.imooc.dto.SysUserAuthDto;
import com.imooc.service.SysUserService;
import com.imooc.util.CommonUtil;
import com.imooc.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 潘畅
 * @date 2018/6/13 14:57
 */
@Controller
@RequestMapping("/")
public class SystemController {
    @Autowired
    private HttpSession session;
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping
    public String init() {
        return  "redirect:/index";
    }
    /**
     * 登录成功后，后台管理首页
     */
    @RequestMapping("/index")
    public String initIndex(Model model) {
        SysUser user = (SysUser) session.getAttribute(SessionKeyConst.SESSION_USER);
        if (user != null){
            model.addAttribute("user", user);
        }
        return "/system/index";
    }
    /**
     * 修改用户密码
     */
    @RequestMapping(value = "/sysUsers/{userId}/password", method = RequestMethod.PUT)
    @ResponseBody
    public ResultDto modifyPassword(@PathVariable("userId") Integer id,
                                    @RequestParam("oldPassword") String oldPassword,
                                    @RequestParam("newPassword") String newPassword,
                                    @RequestParam("newPasswordAgain") String newPasswordAgain){
        if (!CommonUtil.allIsNotBlank(oldPassword, newPassword, newPasswordAgain)){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        SysUser sysUser = sysUserService.getSysUserById(id);
        if (sysUser == null){
            return new ResultDto(PageCodeEnum.USER_NOT_EXISTS);
        }
        if (!sysUser.getPassword().equals(oldPassword)){
            return new ResultDto(PageCodeEnum.OLD_PASSWORD_ERROR);
        }
        if (oldPassword.equals(newPassword)){
            return new ResultDto(PageCodeEnum.NEW_OLD_PASSWORD_SAME);
        }
        if (!newPassword.equals(newPasswordAgain)){
            return new ResultDto(PageCodeEnum.NEW_AGAIN_PASSWORD_NOT_SAME);
        }
        SysUser newSysUser = new SysUser();
        newSysUser.setId(id);
        newSysUser.setPassword(newPassword);
        if (!sysUserService.modifyUser(newSysUser)){
            return new ResultDto(PageCodeEnum.UPDATE_FAILURE);
        }
        return new ResultDto(PageCodeEnum.UPDATE_SUCCESS);
    }

}
