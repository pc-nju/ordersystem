package com.imooc.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * @author 潘畅
 * @date 2018/5/31 20:45
 */
public class Business extends BaseBean {
    private String title;
    private String subtitle;
    private String imgFileName;
    private Integer price;
    private Integer distance;
    /**
     * 已售数量
     */
    private Integer number;
    /**
     * 星级
     */
    private Integer star;
    /**
     * 总评价次数
     */
    private Integer commentNum;
    /**
     * 描述
     */
    private String desc;
    /**
     * 这里对应字典里的代码
     */
    private String city;
    private String category;

    /**
     * 多对一
     * 上面对应字典里的代码，而我们需要显示的是字典中的name，所以这里加了两个Dictionary对象
     */
    private Dictionary cityDictionary;
    private Dictionary categoryDictionary;

    /**
     * 是否为推荐
     */
    @JsonIgnore
    private boolean isRecommended = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Dictionary getCityDictionary() {
        return cityDictionary;
    }

    public void setCityDictionary(Dictionary cityDictionary) {
        this.cityDictionary = cityDictionary;
    }

    public Dictionary getCategoryDictionary() {
        return categoryDictionary;
    }

    public void setCategoryDictionary(Dictionary categoryDictionary) {
        this.categoryDictionary = categoryDictionary;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public int avgStar(){
        int totalCommentNum = commentNum;
        if (totalCommentNum < 1){
            totalCommentNum = 1;
        }
        //四舍五入: 2.3-->2  2.5-->3
        return (int) Math.round(getStar()*1.0/totalCommentNum);
    }
}
