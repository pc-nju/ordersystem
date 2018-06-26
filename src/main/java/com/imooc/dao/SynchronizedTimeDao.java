package com.imooc.dao;

import com.imooc.bean.SynchronizedTime;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author 潘畅
 * @date 2018/6/10 20:49
 */
public interface SynchronizedTimeDao {
    /**
     * 插入同步时间
     * @param synchronizedTime 待插入对象
     */
    void insertSynchronizedTime(SynchronizedTime synchronizedTime);
    /**
     * 更新最后一次的同步时间
     * @param type 同步数据类型
     * @param currentTime 服务器当前时间点
     */
    void updateSynchronizedTime(@Param("type") String type, @Param("currentTime") Date currentTime);

    /**
     * 根据同步数据类型查询最后一次同步时间
     * @param type 同步数据类型
     * @return 同步时间对象
     */
    SynchronizedTime selectSynchronizedTimeByType(String type);
}
