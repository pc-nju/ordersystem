package com.imooc.dto.report;

import java.io.Serializable;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/25 19:13
 */
public class Option implements Serializable {
    /**
     * 1、这里面成员变量名都必须和echarts的option属性中的变量名保持一致，这样才能保证拷贝成功
     * 2、各个成员变量对象包含哪些数据，根据echarts需要的数据需求，进行合理定制
     *     比如：option属性中的xAxis变量，我们在js中已经规定好了type和boundaryGap，所以我们这里只需要提供data属性即可
     */

    private Legend legend;
    private XAxis xAxis;
    private List<Serie> series;

    public Legend getLegend() {
        return legend;
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public XAxis getxAxis() {
        return xAxis;
    }

    public void setxAxis(XAxis xAxis) {
        this.xAxis = xAxis;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }
}
