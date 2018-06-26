package com.imooc.dao;

import com.imooc.bean.Comment;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/12 7:48
 */
public interface CommentDao {
    /**
     * 插入评论
     * @param comment 待插入评论
     * @return 受影响的条数
     */
    int insertComment(Comment comment);

    /**
     * 获取评论列表
     * @param comment 待搜索条件
     * @return 评论列表
     */
    List<Comment> searchByPage(Comment comment);

    /**
     * 删除评论
     * @param comment 待删除评论
     * @return 受影响的条数
     */
    int deleteComment(Comment comment);
}
