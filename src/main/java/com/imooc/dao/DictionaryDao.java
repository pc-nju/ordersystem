package com.imooc.dao;



import com.imooc.bean.Dictionary;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/31 21:43
 */
public interface DictionaryDao {
    /**
     * 根据dictionary查询数据库中的Dictionary集合
     * @param dictionary 带查询对象
     * @return Dictionary集合
     */
    List<Dictionary> selectDictionaries(Dictionary dictionary);
}
