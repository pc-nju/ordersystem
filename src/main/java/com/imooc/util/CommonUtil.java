package com.imooc.util;


import com.imooc.bean.Action;
import com.imooc.constant.DictionaryConstant;
import com.imooc.constant.SessionKeyConst;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import sun.security.provider.MD5;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 潘畅
 * @date 2018/6/8 11:30
 */
public final class CommonUtil {
    /**
     * 生成指定位数的随机整数
     * @param codeLength 长度
     * @return 指定位数的随机整数
     */
    public static int getVerificationCode(int codeLength){
        //Math.pow(double a, double b)：返回 a的b次幂
        return (int)((Math.random() * 9 + 1) * Math.pow(10, codeLength - 1));
    }

    /**
     * 获取UUID
     * @return 随机UUID
     */
    public static String getUUID(){
        //唯一字符串
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断多个字符串是否为null或者""
     * 基本逻辑：只要有一个非空，那么就返回false
     */
    public static boolean allIsBlank(String... values){
        if (values.length > 0){
            for (String value : values){
                if (!StringUtils.isBlank(value)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断多个字符串是否都不为null或者""
     * 基本逻辑：只要有一个空，那么就返回false
     */
    public static boolean allIsNotBlank(String... values){
        if (values.length > 0){
            for (String value : values){
                if (StringUtils.isBlank(value)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断当前用户是否具有某项操作的权限
     * @param session HttpSession
     * @param url 路径
     * @param method 操作：R查   CR 增|查   CRU 增|查|改   CRUD 增|查|改|删
     * @return true 包含该权限 false 无该权限
     */
    public static boolean contain(HttpSession session, String url, String method){
        List<Action> actionList = (List<Action>) session.getAttribute(SessionKeyConst.SESSION_ACTION_INFO);
        if (CollectionUtils.isNotEmpty(actionList)){
            //将method转换成代码
            for (Action action : actionList){
                if (CommonUtil.allIsNotBlank(action.getUrl(), action.getMethod(), url, method)){
                    /*
                     * 举例：
                     *     假设数据库中，广告管理的url是“/ad”：表示访问广告管理的任何功能都可以
                     * method是“CRU”：表示可以增、查、改。
                     *     若目标路径是"/ad/addInit"，method是“U”，那么则可以通过权限检查。
                     *     只要目标路径在数据库的路径之下：url.contains(action.getUrl())
                     *     目标方法在数据库的方法之内：action.getMethod().contains(method)
                     *     即可通过权限检查
                     * 备注：
                     *     注意url和method是包含还是被包含！！
                     *
                     */
                    if (url.contains(action.getUrl()) && action.getMethod().contains(method)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
