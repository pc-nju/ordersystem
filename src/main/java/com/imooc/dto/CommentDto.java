package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.Comment;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 潘畅
 * @date 2018/6/11 11:42
 * 这是向前端展示的
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto extends Comment {
    /*********************** username和comment给前端的 ****************************/
    /**
     * 这里做一个处理，隐藏中间思维手机号
     */
    private String username;
    /**
     * 前端的评论内容是comment字段，后端是content字段
     */
    private String comment;
    /*********************** phone和title给后台的 ****************************/
    private Long phone;
    private String title;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = wrapUsername(username);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 隐藏手机号的中间四位
     * @param username 手机号
     * @return 处理后的手机号
     */
    private String wrapUsername(String username) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(username)){
            sb.append(username.substring(0,3));
            sb.append("****");
            sb.append(username.substring(7));
        }
        if (StringUtils.isBlank(sb.toString())){
            sb.append("***********");
        }
        return sb.toString();
    }
}
