package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.bean.Advertisement;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 潘畅
 * @date 2018/5/20 9:22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvertisementDto extends Advertisement {
    private String img;
    /**
     * 要和前端上传控件的name保持一致，以实现前后端数据绑定
     */
    private MultipartFile imgFile;

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
}
