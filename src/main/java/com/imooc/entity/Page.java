package com.imooc.entity;

import com.imooc.constant.FinalName;

import java.io.Serializable;

/**
 * @author 潘畅
 * @date 2018/5/25 10:10
 */
public class Page implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalNumber;
    private Integer totalPage;
    private Integer dbIndex;
    private Integer dbNumber;

    public Page() {
        this.currentPage = 1;
        this.pageSize = 5;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;

        init(totalNumber);
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(Integer dbIndex) {
        this.dbIndex = dbIndex;
    }

    public Integer getDbNumber() {
        return dbNumber;
    }

    public void setDbNumber(Integer dbNumber) {
        this.dbNumber = dbNumber;
    }

    private void init(Integer totalNumber){
        if (this.pageSize <= 0){
            //默认页数
            this.pageSize = FinalName.DEFAULT_PAGE_SIZE;
        }

        int tempTotalPage = totalNumber / this.pageSize + (totalNumber % this.pageSize == 0 ? 0 : 1);

        if (tempTotalPage < 1){
            tempTotalPage = 1;
        }
        this.totalPage = tempTotalPage;

        if (this.currentPage < 1){
            this.currentPage = 1;
        }
        if (this.currentPage > this.totalPage){
            this.currentPage = this.totalPage;
        }

        this.dbIndex = (this.currentPage - 1) * this.pageSize;
        this.dbNumber = this.pageSize;
    }
}
