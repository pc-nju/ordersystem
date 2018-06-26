package com.imooc.dto;

import com.imooc.constant.PageCodeEnum;

/**
 * @author 咸鱼
 * @date 2018/6/17 21:45
 */
public class PageCodeDto {
    private Integer code;
    private String msg;

    public PageCodeDto(PageCodeEnum pageCodeEnum){
        this.code = Integer.valueOf(pageCodeEnum.getCode());
        this.msg = pageCodeEnum.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
