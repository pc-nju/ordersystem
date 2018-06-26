package com.imooc.constant;

/**
 * @author 潘畅
 * @date 2018/5/20 20:04
 */
public enum PageCodeEnum {
    /**
     * code共4为：前两位10代表新增操作；后两位00代表成功，01代表失败
     */
    SUCCESS("1", "获取数据成功"),
    FAILURE("0", "获取数据失败"),
    ADD_SUCCESS("1000", "新增成功!"),
    ADD_FAILURE("1001", "新增失败!"),
    ADD_FAILURE_SYS_USER("1003", "用户名或别名已存在，请选择其他用户名或别名！"),
    DELETE_SUCCESS("2000", "删除成功！"),
    DELETE_FAILURE("2001", "删除失败！"),
    UPDATE_SUCCESS("3000", "修改成功！"),
    UPDATE_FAILURE("3001", "修改失败"),
    UPDATE_FAILURE_SYS_USER("3002", "修改的用户名或别名已存在，请选择其他用户名或别名！"),
    LOGIN_SUCCESS("4000", "修改成功!"),
    USERNAME_ERROR("4001", "用户名不符合规范！"),
    PASSWORD_ERROR("4002", "密码不符合规范！"),
    LOGIN_FAILURE("4003", "用户名或密码错误，登录失败！"),
    LOGIN_TIMEOUT("4004", "登录超时！"),
    NOTHING_NEEDED_MODIFIED("5001", "没什么需要修改的"),
    USERNAME_EXIST("5002", "该用户名已存在，请更换用户名"),
    NICKNAME_EXIST("5003", "该别名已存在，请更换别名"),
    USER_NOT_EXISTS("5004", "用户不存在！"),
    PARAM_NULL("5005", "前端传递的参数有问题"),
    MENU_NOT_EXISTS("6001", "菜单不存在！"),
    TARGET_MENU_NOT_EXISTS("6002", "目标菜单不存在！"),
    DROP_MENU_NOT_EXISTS("6003", "目标菜单不存在！"),
    ACTION_NOT_EXISTS("6004", "动作不存在！"),
    OLD_PASSWORD_ERROR("6005", "原密码错误！"),
    NEW_OLD_PASSWORD_SAME("6006", "新老密码一致！"),
    NEW_AGAIN_PASSWORD_NOT_SAME("6007", "新密码和再次确认的新密码不一致！");

    private String code;
    private String msg;
    /**
     * 规定调用这个类对象的key
     */
    public static final String KEY = "pageCode";

    PageCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    //因为是常量，注意不能写set()方法，防止别人修改
}
