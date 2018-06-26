package com.imooc.util.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author 潘畅
 * @date 2018/6/9 9:10
 */
public class TokenCache extends BaseGuavaCache<String, Object> {

    private static TokenCache tokenCache;
    /**
     * 在构造函数中初始化缓存过期时间、刷新周期和最大数量
     */
    private TokenCache(){
        //过期时间2min
        this.setExpirationDuration(10);
        this.setExpirationTimeUnit(TimeUnit.MINUTES);
        //这里不让刷新
        this.setRefreshDuration(-1);
        this.setMaxSize(1000);
    }

    /**
     * 单例模式之双重检查锁定
     * @return TokenCache单一实例
     */
    public static TokenCache getInstance(){
        if (tokenCache == null){
            synchronized (TokenCache.class){
                if (tokenCache == null){
                    tokenCache = new TokenCache();
                }
            }
        }
        return tokenCache;
    }

    @Override
    public void loadValueWhenStarted() {

    }

    @Override
    protected Object getValueWhenExpired(String key) throws Exception {
        return getValue(key);
    }
}
