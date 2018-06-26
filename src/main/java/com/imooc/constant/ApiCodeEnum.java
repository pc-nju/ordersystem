package com.imooc.constant;

/**
 * @author 潘畅
 * @date 2018/6/8 10:27
 */
public enum ApiCodeEnum {
    SUCCESS(0, "ok"),
    PARAM_NULL(1, "前端传递的参数有问题"),
    USER_NOT_EXISTS(2, "用户不存在！"),
    REPEAT_REQUEST(3, "验证码有效时间内不需重复请求！"),
    SEND_FAILURE(4, "发送验证码失败，请稍后重试！"),
    CODE_INVALID(5, "验证码失效，请重新请求验证码！"),
    CODE_ERROR(6, "验证码不正确！"),
    NOT_LOGIN(7, "没有登录！"),
    TOKEN_ERROR(8, "登录标识错误！"),
    TOKEN_EXPIRE(9, "登录过期！"),
    BUY_FAILURE(10, "购买失败！"),
    PHONE_ERROR(11, "手机号不符合规范！"),
    ORDER_NOT_EXISTS(12, "订单不存在！"),
    ORDER_NOT_YOURS(13, "不是您的订单，您无权评论！"),
    COMMENT_ERROR(14, "评论失败！"),
    UPDATE_ORDER_ERROR(15, "更新订单失败！"),
    NO_AUTH(14, "没有权限！");

    private Integer errno;
    private String msg;

    ApiCodeEnum(Integer errno, String msg) {
        this.errno = errno;
        this.msg = msg;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
