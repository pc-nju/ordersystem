package com.imooc.dto;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/11 14:52
 */
public class CommentListDto {
    private boolean hasMore;
    private List<CommentDto> data;

    public CommentListDto() {
        hasMore = false;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<CommentDto> getData() {
        return data;
    }

    public void setData(List<CommentDto> data) {
        this.data = data;
    }
}
