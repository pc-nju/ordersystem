package com.imooc.dto;

import com.imooc.constant.ApiCodeEnum;

/**
 * @author 潘畅
 * @date 2018/6/8 10:31
 */
public class ApiCodeDto {
    private Integer errno;
    private String msg;
    private String code;
    private String token;

    public Integer getErrno() {
        return errno;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ApiCodeDto(ApiCodeEnum apiCodeEnum) {
        this.errno = apiCodeEnum.getErrno();
        this.msg = apiCodeEnum.getMsg();
    }
    public ApiCodeDto(ApiCodeEnum apiCodeEnum, String code) {
        this.errno = apiCodeEnum.getErrno();
        this.msg = apiCodeEnum.getMsg();
        this.code = code;
    }
}
