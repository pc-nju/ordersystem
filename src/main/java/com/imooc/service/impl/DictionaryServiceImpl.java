package com.imooc.service.impl;

import com.imooc.bean.Dictionary;
import com.imooc.dao.DictionaryDao;
import com.imooc.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/21 19:44
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public List<Dictionary> getDictionaryListByType(String dictionaryType) {
        Dictionary dictionary = new Dictionary();
        dictionary.setType(dictionaryType);
        return dictionaryDao.selectDictionaries(dictionary);
    }
}
