package com.imooc.service;

import com.imooc.bean.Dictionary;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/21 19:44
 */
public interface DictionaryService {
    /**
     * 根据字典类型获取字典数据集合
     * @param dictionaryType 字典数据类型
     * @return 字典数据集合
     */
    List<Dictionary> getDictionaryListByType(String dictionaryType);
}
