package com.imooc.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/25 19:04
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XAxis implements Serializable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
