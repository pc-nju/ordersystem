package com.imooc.dao;


import com.imooc.bean.Business;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/1 20:07
 */
public interface BusinessDao {
    /**
     * 插入商户记录
     * @param business 待插入对象
     * @return 返回在数据库中受影响的条数
     */
    int insertBusiness(Business business);

    /**
     * 根据关键字，搜索商户
     * @param business 待搜索对象
     * @return 商户集合
     */
    List<Business> searchByPage(Business business);

    /**
     * 根据指定条件获取商户集合
     * @param business 待搜索对象
     * @return 商户集合
     */
    List<Business> selectBusinesses(Business business);

    /**
     * 删除商户
     * @param business 待删除对象
     * @return 影响的条数
     */
    int deleteBusiness(Business business);

    /**
     * 修改商户信息
     * @param business 待修改对象
     * @return 受影响的条数
     */
    int updateBusiness(Business business);

    /**
     * 根据id查询商户
     * @param id 商户id
     * @return 商户
     */
    Business selectBusinessesById(Integer id);

    /**
     * 同步商品已售数量
     * @param currentTime 当前时间
     * @param lastUpdateTime 最后一次同步时间
     */
    void synchronizedNumber(@Param("currentTime") Date currentTime, @Param("lastUpdateTime")Date lastUpdateTime);

    /**
     * 同步商品星级
     * @param currentTime 当前时间
     * @param lastUpdateTime 最后一次同步时间
     */
    void synchronizedStar(@Param("currentTime") Date currentTime, @Param("lastUpdateTime")Date lastUpdateTime);
}
