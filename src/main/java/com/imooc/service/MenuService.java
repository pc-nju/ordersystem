package com.imooc.service;

import com.imooc.bean.Menu;
import com.imooc.dto.MenuDto;
import com.imooc.dto.MoveNodeForZtreeDto;
import com.imooc.dto.ResultDto;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/20 17:26
 */
public interface MenuService {
    /**
     * 获取所有的菜单
     * @return 菜单集合
     */
    ResultDto getAllMenus();

    /**
     * 根据菜单id，获取指定菜单
     * @param id 菜单id
     * @return 状态码
     */
    ResultDto getMenuWithId(Integer id);

    /**
     * 根据id删除指定菜单
     * @param id 菜单id
     * @return 状态码
     */
    ResultDto removeMenuWithId(Integer id);

    /**
     * 修改指定菜单
     * @param menuDto 待修改条件
     * @return 状态码
     */
    ResultDto modifyMenu(MenuDto menuDto);

    /**
     * 新增菜单
     * @param menuDto 待新增对象
     * @return 状态码
     */
    ResultDto addMenu(MenuDto menuDto);

    /**
     * 拖动节点成为某个菜单的子节点
     * @param moveNodeForZtreeDto 封装前端传送的参数对象
     * @return 状态码
     */
    ResultDto dropNode(MoveNodeForZtreeDto moveNodeForZtreeDto);

    /**
     * 获取Menu集合
     * @param menu 条件
     * @return Menu集合
     */
    List<Menu> getMenu(Menu menu);
}
