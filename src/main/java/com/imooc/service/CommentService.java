package com.imooc.service;

import com.imooc.constant.PageCodeEnum;
import com.imooc.dto.ApiCodeDto;
import com.imooc.dto.CommentListDto;
import com.imooc.dto.CommentOrderDto;
import org.springframework.ui.Model;


/**
 * @author 潘畅
 * @date 2018/6/12 7:47
 */
public interface CommentService {
    /**
     * 提交评论
     * @param commentOrderDto 评论对象
     * @return 评论结果
     */
    ApiCodeDto submitComment(CommentOrderDto commentOrderDto);

    /**
     * 获取评论列表
     * @param currentPage 当前页
     * @param businessId 商户id
     * @return 评论列表和hasMore标识
     */
    CommentListDto getCommentListForApi(Integer currentPage, Integer businessId);

    /**
     * 搜索评论
     * @param searchKey 搜索关键词
     * @param currentPage 当前页
     * @param model 数据模型
     */
    void search(String searchKey, Integer currentPage, Model model);

    /**
     * 删除评论
     * @param id 评论id
     * @return 删除结果
     */
    PageCodeEnum deleteCommentWithId(Integer id);
}
