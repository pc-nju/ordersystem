package com.imooc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author 潘畅
 * @date 2018/5/19 20:12
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Advertisement extends BaseBean {
    private String title;
    /**
     * 问题：为什么这里不设计成图片地址（imgUrl）?
     * 原因：
     *     对于并发访问量大的应用，专门有服务器集群来存放各种静态资源（比如图片）是很正常的事。有时候很容易一台服务器崩
     * 溃了，其他服务器要顶上，这个时候快速更换“访问路径”是很常见的事。“访问路径”其实变的只是存放文件的“文件夹”，
     * 不变的就是文件的名称。所以通常的做法是保存文件的文件名，将“文件夹”的地址写在配置文件中，当我们接收前端传过来的
     * 文件时，读取配置文件中的“文件夹地址”，和数据库中文件的“文件名”进行拼接，即可得到上传文件的保存路径。这样做高效
     * 、简单！
     *     关于图片的访问路径，同理！将“访问路径”的前缀写在配置文件中！
     */
    private String imgFileName;
    private String link;
    private Integer weight;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
