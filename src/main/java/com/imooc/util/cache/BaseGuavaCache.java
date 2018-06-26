package com.imooc.util.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *     利用guava实现的内存缓存。缓存加载之后永不过期，后台线程定时刷新缓存值。刷新失败时将继续返回旧缓存。
 * 在调用getValue之前，需要设置 refreshDuration， refreshTimeunit， maxSize 三个参数后台刷新线程池为该系
 * 统中所有子类共享，大小为20.
 * @author **
 * @date 2018/6/8 15:07
 */
public abstract class BaseGuavaCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseGuavaCache.class);

    /**
     * 缓存自动刷新周期
     */
    private int refreshDuration = 10;
    /**
     * 缓存自动刷新周期时间格式
     */
    private TimeUnit refreshTimeUnit = TimeUnit.MINUTES;

    /**
     * 缓存过期周期（负数代表永不过期）
     */
    private int expirationDuration = -1;
    /**
     * 缓存过期周期时间格式
     */
    private TimeUnit expirationTimeUnit = TimeUnit.HOURS;
    /**
     * 缓存最大容量
     * 备注：maxiSize定义了缓存的容量大小，当缓存数量即将到达容量上线时，则会进行缓存回收，
     *       回收最近没有使用或总体上很少使用的缓存项。需要注意的是在接近这个容量上限时就
     *       会发生，所以在定义这个值的时候需要视情况适量地增大一点。
     */
    private int maxSize = 4;
    /**
     * 缓存刷新线程池
     */
    private static ListeningExecutorService  refreshThreadPool =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));
    /**
     * 使用双重检查锁，设置为单例
     */
    private LoadingCache<K, V> cache = null;

    /**
     * 用于初始化缓存（某些场景下使用，例如系统启动检测缓存加载是否正常）
     */
    public abstract void loadValueWhenStarted();

    /**
     * 定义缓存值过期时的计算方法：一般是缓存过期时，重新读取数据库，缓存数据库中的数值（具体视情况而变）
     *     新值计算失败时抛出异常，get操作将继续返回旧的缓存
     * @param key 缓存的key
     * @return 缓存值
     * @throws Exception 异常
     */
    protected abstract V getValueWhenExpired(K key) throws Exception;

    private LoadingCache<K, V> getCache(){
        //单例模式中的双重检查锁
        if (cache == null){
            synchronized (this){
                if (cache == null){
                    CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder().maximumSize(maxSize);
                    //设置缓存刷新周期
                    if (refreshDuration > 0){
                        cacheBuilder.refreshAfterWrite(refreshDuration, refreshTimeUnit);
                    }
                    //设置缓存过期周期
                    if (expirationDuration > 0){
                        cacheBuilder.expireAfterWrite(expirationDuration, expirationTimeUnit);
                    }
                    cache = cacheBuilder.build(new CacheLoader<K, V>() {
                        @Override
                        public V load(K k) throws Exception {
                            //为null时，会抛出异常
                            return getValueWhenExpired(k);
                        }

                        @Override
                        public ListenableFuture<V> reload(final K key, V oldValue) throws Exception {
                            return refreshThreadPool.submit(new Callable<V>() {
                                @Override
                                public V call() throws Exception {
                                    return getValueWhenExpired(key);
                                }
                            });
                        }
                    });
                }
            }
        }
        return cache;
    }

    /**
     * 从cache中取数据
     * @param key 键
     * @return 值
     * @throws Exception 异常
     * 备注：该key对应的值在缓存中不存在或者过期，调用该方法就会抛出异常。这个方法必须显式抛出异常，
     *       以供业务层判断缓存是否存在以及是否过期
     */
    public V getValue(K key) throws Exception {
        try {
            return getCache().get(key);
        } catch (ExecutionException e){
            LOGGER.error("从内存缓存中获取内容时发生异常，key:" + key, e);
            throw e;
        }
    }

    /**
     * 从cache中取数据，若发生异常，则返回默认值
     * @param key 键
     * @return 值
     * @throws ExecutionException 异常
     */
    public V getValueOfDefault(K key, V defaultValue){
        try {
            return getCache().get(key);
        } catch (ExecutionException e) {
            LOGGER.error("从内存缓存中获取内容时发生异常，key:" + key, e);
            return defaultValue;
        }
    }
    public void put(K key, V value){
        cache.put(key, value);
    }

    /**
     * 设置缓存刷新周期(链式编程)
     */
    protected BaseGuavaCache<K, V> setRefreshDuration(int refreshDuration) {
        this.refreshDuration = refreshDuration;
        return this;
    }
    /**
     * 设置缓存刷新周期时间单元(链式编程)
     */
    protected void setRefreshTimeUnit(TimeUnit refreshTimeUnit) {
        this.refreshTimeUnit = refreshTimeUnit;
    }
    /**
     * 设置缓存过期周期(链式编程)
     */
    protected void setExpirationDuration(int expirationDuration) {
        this.expirationDuration = expirationDuration;
    }
    /**
     * 设置缓存过期周期时间单元(链式编程)
     */
    protected void setExpirationTimeUnit(TimeUnit expirationTimeUnit) {
        this.expirationTimeUnit = expirationTimeUnit;
    }
    /**
     * 设置缓存最大数量(链式编程)
     */
    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache(){
        this.getCache().invalidateAll();
    }
    /**
     * 清除指定缓存
     */
    public void clearCacheByKey(K key){
        this.getCache().invalidate(key);
    }

}
