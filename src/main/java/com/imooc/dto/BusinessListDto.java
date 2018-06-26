package com.imooc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/4 9:35
 */
public class BusinessListDto {
    private boolean hasMore;
    private List<BusinessDto> data;

    public BusinessListDto() {
        this.hasMore = false;
        this.data = new ArrayList<>();
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<BusinessDto> getData() {
        return data;
    }

    public void setData(List<BusinessDto> data) {
        this.data = data;
    }
}
