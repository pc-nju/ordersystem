package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.Business;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 潘畅
 * @date 2018/5/31 20:46
 * Business是和数据库交互的
 * BusinessDto是和前端交互的，可以根据需要增加字段
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessDto extends Business{
    private String img;
    private MultipartFile imgFile;
    private Integer mumber;
    private String subTitle;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MultipartFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }

    public Integer getMumber() {
        return mumber;
    }

    public void setMumber(Integer mumber) {
        this.mumber = mumber;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
