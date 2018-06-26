package com.imooc.dao;

import com.imooc.bean.Menu;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/20 17:26
 */
public interface MenuDao {
    /**
     * 根据条件获取菜单集合
     * @param menu 条件
     * @return 菜单
     */
    List<Menu> selectMenu(Menu menu);

    /**
     * 根据菜单id获取菜单id
     * @param id 菜单id
     * @return 菜单
     */
    Menu selectMenuWithId(Integer id);

    /**
     * 删除Menu
     * @param menu 待删除对象
     */
    void deleteMenu(Menu menu);

    /**
     * 更新菜单
     * @param menu 待更新条件
     * @return 受影响的行数
     */
    int updateMenu(Menu menu);

    /**
     * 新增菜单
     * @param menu 待新增对象
     * @return 受影响的行数
     */
    int insertMenu(Menu menu);

    /**
     *     根据菜单id，更新其parentId，同时将该菜单的orderNum(排序值)设置为父菜单（parentId）下所有的子菜单的
     * orderNum(排序值)+1，保证排在父节点下的第一位
     * @param menu 待更新对象
     * @return 受影响的行数
     */
    int updateDropMenu(Menu menu);
}
