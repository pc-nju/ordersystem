package com.imooc.service;

import com.imooc.dto.AdvertisementDto;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/20 9:37
 */
public interface AdvertisementService {
    /**
     * 插入广告
     * @param advertisementDto 新增广告
     * @return true新增成功;false失败
     */
    boolean addAdvertisement(AdvertisementDto advertisementDto);


    /**
     * 分页查询广告
     * @param title 标题
     * @param currentPage 当前页码
     * @param model 前端数据集合
     * @return 广告集合
     */
    List<AdvertisementDto> searchByPage(String title, Integer currentPage, Model model);

    /**
     * 删除广告
     * @param id 待删除的广告id
     * @return 是否成功
     */
    boolean deleteAdvertisement(Integer id);

    /**
     * 修改广告初始化
     * @param id 广告id
     * @return 待修改对象
     */
    AdvertisementDto modifyInit(Integer id);

    /**
     * 修改广告
     * @param advertisementDto 待修改对象
     * @return 是否成功
     */
    boolean modifyAdvertisement(AdvertisementDto advertisementDto);
}
