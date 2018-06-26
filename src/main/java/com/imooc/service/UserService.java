package com.imooc.service;

import com.imooc.dto.ApiCodeDto;

/**
 * @author 潘畅
 * @date 2018/6/8 10:56
 */
public interface UserService {
    /**
     * 根据手机号，发送短信验证码
     * @param phone 手机号
     * @return 处理结果
     */
    ApiCodeDto sendSmsVerificationCode(Long phone);

    /**
     * 登录
     * @param phone 手机号
     * @param verificationCode 验证码
     * @return 登录结果
     */
    ApiCodeDto login(Long phone, String verificationCode);
}
