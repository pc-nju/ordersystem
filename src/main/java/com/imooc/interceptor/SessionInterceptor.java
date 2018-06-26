package com.imooc.interceptor;

import com.imooc.bean.*;
import com.imooc.constant.MenuConst;
import com.imooc.constant.SessionKeyConst;
import com.imooc.dao.*;
import com.imooc.dto.ActionDto;
import com.imooc.dto.MenuDto;
import com.imooc.util.Md5Util;
import com.imooc.util.SpringContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 拦截Session，判断是否登录（HandlerInterceptor是Spring提供的拦截器，不是原生JDK的拦截器）
 * @author 潘畅
 * @date 2018/6/13 11:37
 */
public class SessionInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionInterceptor.class);
    /**
     * 在进入Handler方法（就是Controller中映射路径对应的方法）执行之前执行本方法
     * @return  true：执行下一个拦截器，直到所有的拦截器都执行完毕，再执行拦截的Controller中的Handler方法
     *         false：从当前的拦截器往回执行所有拦截器的afterCompletion()方法，再退出拦截器链，不再执行Handler方法
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        //若Session中有User，说明用户已经登录，可以继续执行后续代码
        if (session.getAttribute(SessionKeyConst.SESSION_USER) != null){
            pushMenuInfoInSession(session);
            return true;
        }
        //验证Cookie中是否有登陆标识
        if (validateLoginWithCookie(httpServletRequest)){
            pushMenuInfoInSession(session);
            return true;
        }
        /*
         *     判断是否是Ajax请求（主要看请求头中是否有"X-Requested-With"），若是，则将跳转路径放到Header中。
         * 这里我做了一个统一的处理，所有的Session超时，统一由请求"/login/sessionTimeout"
         */
        if (httpServletRequest.getHeader("X-Requested-With") != null){
            String basePath = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName()
                    + ":" +httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
            httpServletResponse.addHeader("SessionTimeoutPath", basePath + "/login/sessionTimeout");
        }

        //若Session中没有User，说明用户未登录或登录超时，跳转到登录界面
        httpServletRequest.getRequestDispatcher("/login/sessionTimeout").forward(httpServletRequest, httpServletResponse);
        return false;
    }

    /**
     * 将用户可操作的菜单放到Session中
     * @param session httpSession
     */
    private void pushMenuInfoInSession(HttpSession session) {
        if (session.getAttribute(SessionKeyConst.SESSION_MENU_INFO) == null){
            SysUser sysUser = (SysUser) session.getAttribute(SessionKeyConst.SESSION_USER);
            //若groupId不为null，则说明已经分配了用户组
            if (sysUser.getGroupId() != null){
                GroupDao groupDao = SpringContextHolder.getBean(GroupDao.class);
                Group group = groupDao.selectGroupMenusInfoWithId(sysUser.getGroupId());
                session.setAttribute(SessionKeyConst.SESSION_MENU_INFO, group.getMenuList());
                //因为并不一定该Menu下的所有Action都会分配给某个用户组，所以能操作的Action也要单独放到Session中
                session.setAttribute(SessionKeyConst.SESSION_ACTION_INFO, group.getActionList());
            }
        }
    }

    private boolean validateLoginWithCookie(HttpServletRequest httpServletRequest) {
        String loginToken = "";
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null && cookies.length >0){
            for (Cookie cookie : cookies){
                if (SessionKeyConst.SESSION_LOGIN_TOKEN.equals(cookie.getName())){
                    loginToken = cookie.getValue();
                }
            }
        }
        if (!loginToken.trim().equals("")){
            //切割出userId
            String[] strs = loginToken.split(":");
            String md5Str = strs[0];
            String userId = strs[1];
            //因为拦截器启动的时候，SysUserDao还未注入到Spring容器中，所以通过@AutoWired无法注入SysUserDao对象
            SysUserDao sysUserDao = SpringContextHolder.getBean(SysUserDao.class);
            SysUser sysUser = sysUserDao.selectUserWithId(Integer.parseInt(userId));
            if (sysUser != null){
                //因为这个时候，LoginServiceImpl不一定能注入到Spring容器中，所以它生成token的方法这里没法调用，老老实实自己写
                String str = Md5Util.getMD5WithSalt(sysUser.getUsername()+sysUser.getPassword(), SessionKeyConst.ENCRYPT_SALT);
                //经过相同加密手段得到的字符串相同，则说明通过验证
                if (md5Str.equals(str)){
                    //构建Session，供服务器内做已登录跳转（这步必须做，否则内部跳转还是需要登录！！）
                    httpServletRequest.getSession().setAttribute(SessionKeyConst.SESSION_USER, sysUser);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在进入Handler方法之后，返回ModelAndView之前执行（就是拿到了Handler方法的返回值之后）
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 在Handler方法执行完之后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
