package com.imooc.dao;

import com.imooc.bean.Action;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/20 17:42
 */
public interface ActionDao {
    /**
     * 根据条件获取Action集合
     * @param action 条件
     * @return Action集合
     */
    List<Action> selectActionList(Action action);

    /**
     * 删除Action
     * @param action 待删除条件
     * @return 受影响的行数
     */
    int deleteAction(Action action);

    /**
     * 新增Action
     * @param action 待新增对象
     * @return 受影响的行数
     */
    int insertAction(Action action);
    /**
     * 更新Action
     * @param action 待更新对象
     * @return 受影响的行数
     */
    int updateAction(Action action);
}
