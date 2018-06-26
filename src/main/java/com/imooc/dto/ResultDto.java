package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.constant.PageCodeEnum;

/**
 * @author 咸鱼
 * @date 2018/6/19 8:31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDto {
    private Integer code;
    private String msg;
    private Object data;
    private Object option;
    public ResultDto() {
    }

    public ResultDto(PageCodeEnum pageCodeEnum){
        this.code = Integer.valueOf(pageCodeEnum.getCode());
        this.msg = pageCodeEnum.getMsg();
    }

    public ResultDto(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultDto data(Object data){
        this.data = data;
        return this;
    }

    public ResultDto success(){
        this.code = Integer.valueOf(PageCodeEnum.SUCCESS.getCode());
        this.msg = PageCodeEnum.SUCCESS.getMsg();
        //链式编程（有了这个还可以继续调用“set()”方法，填充“data”）
        return this;
    }

    public ResultDto failure(){
        this.code = Integer.valueOf(PageCodeEnum.FAILURE.getCode());
        this.msg = PageCodeEnum.FAILURE.getMsg();
        //链式编程（有了这个还可以继续调用“set()”方法，填充“data”）
        return this;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getOption() {
        return option;
    }

    public ResultDto setOption(Object option) {
        this.option = option;
        return this;
    }
}
