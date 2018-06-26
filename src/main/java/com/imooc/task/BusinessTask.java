package com.imooc.task;

import com.imooc.bean.SynchronizedTime;
import com.imooc.constant.SynchronizedTimeConstant;
import com.imooc.dao.BusinessDao;
import com.imooc.dao.SynchronizedTimeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author **
 * @date 2018/6/10 19:42
 *     “@Component("businessTask")”注解：这里面的value("businessTask"),我们在配置文件中需要使用该值，用以
 * 指向任务调度类（BusinessTask）
 */
@Component("businessTask")
public class BusinessTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessTask.class);
    @Resource
    private BusinessDao businessDao;
    @Resource
    private SynchronizedTimeDao synchronizedTimeDao;
    /**
     * 同步商品已售数量
     * 核心思想：
     *     1、建一张同步表，表中记录各种类型数据最后一次同步时间
     *     2、根据数据类型从同步表中取出最后一次同步时间
     *         （1）若为NULL，则说明该类型数据还未同步过，所以需要查询所有订单，联合商户表，更新商户已售数量
     *         （2）不为NULL，同步订单，条件：最后一次同步时间 < 订单创建时间 <= 当前时间
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void synchronizedNumber(){
        LOGGER.info("商品已售数量开始同步");
        //取出“商户已售数量”最后一次同步时间
        Date lastUpdateTime = getLastSynchronizedTime(SynchronizedTimeConstant.BUSINESS_SOLD_NUMBER);
        //以当前时间为节点，当前时间及其以前的为待同步对象
        /*
         *  首先更新同步时间
         *  问题：为什么不是先同步，再更新同步时间？
         *  原因：因为同步是需要一定时间的，而在我们同步的时候，还会有新的数据产生。若我们以同步结束时间点为界，那么在
         *        开始同步到同步结束这段时间产生的数据在下一次同步操作中，也不会被同步。因为我们下次同步是以上次同步
         *        结束时间点为开始查询条件的。
         *        所以，这里统一以服务器当前时间为准（执行到sql语句也需要一定时间，所以若我们使用sql语句中的当前时间，
         *        还是有时间差）
         */
        Date currentTime = new Date(System.currentTimeMillis());
        if (lastUpdateTime == null){
            //若为null，说明暂未该同步记录，新增同步记录
            SynchronizedTime synchronizedTime = new SynchronizedTime();
            synchronizedTime.setType(SynchronizedTimeConstant.BUSINESS_SOLD_NUMBER);
            //最后一次同步时间就是执行完本次同步的当前时间
            synchronizedTime.setUpdateTime(currentTime);
            synchronizedTimeDao.insertSynchronizedTime(synchronizedTime);
        } else {
            //若不为null，说明本来又该类型数据同步记录，只要更新最后一次同步时间为当前时间即可
            synchronizedTimeDao.updateSynchronizedTime(SynchronizedTimeConstant.BUSINESS_SOLD_NUMBER, currentTime);
        }
        //开始同步
        businessDao.synchronizedNumber(currentTime, lastUpdateTime);
        LOGGER.info("商品已售数量同步结束");
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void synchronizedStar(){
        LOGGER.info("商品评价星级开始同步");
        //取出“商户已售数量”最后一次同步时间
        Date lastUpdateTime = getLastSynchronizedTime(SynchronizedTimeConstant.BUSINESS_COMMENT_STAR);
        //以当前时间为节点，当前时间及其以前的为待同步对象
        Date currentTime = new Date(System.currentTimeMillis());
        if (lastUpdateTime == null){
            //若为null，说明暂未该同步记录，新增同步记录
            SynchronizedTime synchronizedTime = new SynchronizedTime();
            synchronizedTime.setType(SynchronizedTimeConstant.BUSINESS_COMMENT_STAR);
            //最后一次同步时间就是执行完本次同步的当前时间
            synchronizedTime.setUpdateTime(currentTime);
            synchronizedTimeDao.insertSynchronizedTime(synchronizedTime);
        } else {
            //若不为null，说明本来有该类型数据同步记录，只要更新最后一次同步时间为当前时间即可
            synchronizedTimeDao.updateSynchronizedTime(SynchronizedTimeConstant.BUSINESS_COMMENT_STAR, currentTime);
        }
        //开始同步
        businessDao.synchronizedStar(currentTime, lastUpdateTime);
        LOGGER.info("商品评价星级同步结束");
    }


    private Date getLastSynchronizedTime(String type) {
        Date lastUpdateTime;
        //根据同步数据类型，取出该类型最后一次同步时间
        SynchronizedTime synchronizedTime = synchronizedTimeDao.selectSynchronizedTimeByType(type);
        if (synchronizedTime == null){
            lastUpdateTime = null;
        } else {
            lastUpdateTime = synchronizedTime.getUpdateTime();
        }
        return lastUpdateTime;
    }
}
