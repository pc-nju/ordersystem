package com.imooc.bean;

import com.imooc.entity.Page;

import java.io.Serializable;

/**
 * @author 潘畅
 * @date 2018/5/19 20:12
 */
public class BaseBean implements Serializable {
    protected Integer id;

    private Page page;

    private String searchKey;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj instanceof BaseBean){
            BaseBean anotherBaseBean = (BaseBean) obj;
            return this.id != null && this.id.equals(anotherBaseBean.id);
        }
        return super.equals(obj);
    }
}
