package com.imooc.controller;

import com.imooc.constant.SessionKeyConst;
import com.imooc.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author 咸鱼
 * @date 2018/6/24 17:02
 */
@Controller
@RequestMapping("/session")
public class SessionController {
    @Autowired
    private HttpSession session;

    @ResponseBody
    @RequestMapping("/menus")
    public ResultDto getMenusFromSession(){
        if (session.getAttribute(SessionKeyConst.SESSION_MENU_INFO) == null){
            return new ResultDto().failure();
        }
        return new ResultDto().success().data(session.getAttribute(SessionKeyConst.SESSION_MENU_INFO));
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteSession(){
        if (session.getAttribute(SessionKeyConst.SESSION_MENU_INFO) != null){
            session.setAttribute(SessionKeyConst.SESSION_MENU_INFO, null);
        }
        return "redirect:/login";
    }
}
