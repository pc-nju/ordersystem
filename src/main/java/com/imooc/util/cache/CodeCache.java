package com.imooc.util.cache;


import java.util.concurrent.TimeUnit;

/**
 * @author 潘畅
 * @date 2018/6/8 17:17
 */
public class CodeCache extends BaseGuavaCache<String, Object> {

    private static CodeCache codeCache;

    /**
     * 单例模式
     */
    public static CodeCache getInstance() {
        if (codeCache == null){
            synchronized (CodeCache.class){
                if (codeCache == null){
                    codeCache = new CodeCache();
                }
            }
        }
        return codeCache;
    }

    /**
     * 在这里初始化必要参数（比如过期时间，定期刷新时间，缓存最大条数等）
     */
    private CodeCache() {
        //初始化过期时间
        this.setExpirationDuration(1);
        this.setExpirationTimeUnit(TimeUnit.MINUTES);
        //不刷新缓存
        this.setRefreshDuration(-1);
        this.setMaxSize(1000);
    }

    @Override
    public void loadValueWhenStarted() {

    }

    /**
     * 关于这个方法，暂时我也不太清楚，只能暂时先调用“getValue(key)”方法
     */
    @Override
    protected Object getValueWhenExpired(String key) throws Exception {
        return getValue(key);
    }

}
