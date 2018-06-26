package com.imooc.dao;

import com.imooc.bean.Advertisement;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/20 9:24
 */
public interface AdvertisementDao {
    /**
     * 插入广告
     * @param advertisement Advertisement对象
     */
    void insertAdvertisement(Advertisement advertisement);

    /**
     * 分页查询广告
     * @param advertisement 待查询对象
     * @return 广告集合
     */
    List<Advertisement> searchByPage(Advertisement advertisement);

    /**
     * 返回删除的条数
     * @param advertisement 广告对象
     * @return 返回被删除的条数
     */
    int deleteAdvertisement(Advertisement advertisement);
    /**
     * 查询广告
     * @param advertisement 待查询对象
     * @return 广告集合
     */
    List<Advertisement> selectAdvertisements(Advertisement advertisement);

    /**
     * 更新广告
     * @param advertisement 待更新对象
     * @return 更新的条数
     */
    int updateAdvertisement(Advertisement advertisement);
}
