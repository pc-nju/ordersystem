package com.imooc.service;

import com.imooc.bean.Dictionary;
import com.imooc.dto.BusinessDto;
import com.imooc.dto.BusinessListDto;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/31 21:33
 */
public interface BusinessService {
    /**
     * 通过字典类型，获取字典集合
     * @param dictionaryType 字典类型
     * @return 字典集合
     */
    List<Dictionary> getDictionariesByType(String dictionaryType);

    /**
     * 录入商户信息
     * @param businessDto 待录入商户对象
     * @return 是否录入成功
     */
    boolean addBusiness(BusinessDto businessDto);

    /**
     * 查询所有商户记录的第一页
     * @return 商户记录集合第一页
     * @param model 数据模型
     */
    List<BusinessDto> searchBusinessByPage(Model model);

    /**
     * 根据关键词搜索商户
     * @param searchKey 关键词
     * @param city 城市
     * @param category 类别
     * @param currentPage 当前页
     * @param model 数据模型
     * @return 返回商户集合
     */
    List<BusinessDto> searchBusinessByPage(String searchKey, String city, String category, Integer currentPage, Model model);

    /**
     * 删除商户
     * @param id 商户id
     * @return 是否删除成功
     */
    boolean deleteBusiness(Integer id);

    /**
     * 根据商户ID找商户
     * @param id 商户id
     * @return 商户对象
     */
    BusinessDto searchBusinessById(Integer id);

    /**
     * 修改商户信息
     * @param businessDto 待修改商户
     * @return 是否修改成功
     */
    boolean modifyBusiness(BusinessDto businessDto);

    /**
     * 搜索商户
     * @param businessDto 待搜索条件
     * @return 商户集合结果集
     */
    BusinessListDto searchBusinessForApi(BusinessDto businessDto);

    /**
     * 推荐商户
     * @param businessDto 待推荐条件
     * @return 商户集合结果集
     */
    BusinessListDto searchHomeListForApi(BusinessDto businessDto);

    /**
     * 查询商户详情
     * @param businessDto 待搜索条件
     * @return 商户详情
     */
    BusinessDto searchBusinessDetailForApi(BusinessDto businessDto);
}
