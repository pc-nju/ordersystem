package com.imooc.service;


import com.imooc.bean.Action;
import com.imooc.dto.ActionDto;
import com.imooc.dto.ResultDto;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/20 17:41
 */
public interface ActionService {
    /**
     * 根据Action的id，获取指定Action
     * @param id Action的id
     * @return 状态码
     */
    ResultDto getActionWithId(Integer id);
    /**
     * 根据id删除指定Action
     * @param id Action的id
     * @return 状态码
     */
    ResultDto removeActionWithId(Integer id);
    /**
     * 修改指定Action
     * @param actionDto 待修改条件
     * @return 状态码
     */
    ResultDto modifyAction(ActionDto actionDto);

    /**
     * 新增Action
     * @param actionDto 待新增对象
     * @return 状态码
     */
    ResultDto addAction(ActionDto actionDto);

    /**
     * 根据菜单id，删除所有该菜单下的Action
     * @param menuId 菜单id
     */
    void deleteActionByMenuId(Integer menuId);

    /**
     * 根据actionId，修改menuId
     * @param actionId Action的id
     * @param menuId 菜单id
     * @return 受影响的行数
     */
    int modifyActionMenuIdById(Integer actionId, Integer menuId);
}
