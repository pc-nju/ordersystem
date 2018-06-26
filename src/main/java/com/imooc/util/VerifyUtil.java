package com.imooc.util;

import com.imooc.util.cache.TokenCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 潘畅
 * @date 2018/6/11 11:49
 * 登录token验证工具类
 */
public final class VerifyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyUtil.class);
    /**
     * 验证token
     * @param phoneNumber 电话
     * @param token 待验证token
     * @return 验证结果
     */
    public static int verifyToken(String phoneNumber, String token) {

        TokenCache tokenCache = TokenCache.getInstance();
        try {
            String tokenInCache = (String) tokenCache.getValue(phoneNumber);
            if (tokenInCache.equals(token)){
                return 0;
            } else {
                LOGGER.info("该用户：" + phoneNumber + "登录凭证错误！");
                return 1;
            }
        } catch (Exception e) {
            LOGGER.info("该用户：" + phoneNumber + "登录凭证已过期！");
            return 2;
        }
    }

    /**
     * 校验手机号是否符合规则
     */
    public static boolean verifyPhoneNumber(String phoneNumber){
        String phoneNumberRegex = "^[1][0-9]{10}$";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.find()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验用户名（这里只校验了长度）
     * @param username 用户名
     * @return true：符合规范 false：不符合规范
     */
    public static boolean validateUsername(String username) {
        if (!StringUtils.isBlank(username) && username.length() >=2 && username.length() <= 40){
            return true;
        }
        return false;
    }
    /**
     * 校验密码
     * @param password 密码（这里只校验了长度）
     * @return true：符合规范 false：不符合规范
     */
    public static boolean validatePassword(String password) {
        if (!StringUtils.isBlank(password) && password.length() >=3 && password.length() <= 40){
            return true;
        }
        return false;
    }
}
