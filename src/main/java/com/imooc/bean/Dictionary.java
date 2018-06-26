package com.imooc.bean;


/**
 * @author 潘畅
 * @date 2018/5/31 20:22
 */
public class Dictionary extends BaseBean {
    private String type;
    private String code;
    private String name;
    private String weight;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
