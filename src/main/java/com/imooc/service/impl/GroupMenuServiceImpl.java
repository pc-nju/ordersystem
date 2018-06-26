package com.imooc.service.impl;

import com.imooc.bean.GroupMenu;
import com.imooc.dao.GroupMenuDao;
import com.imooc.service.GroupMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 咸鱼
 * @date 2018/6/22 14:31
 */
@Service
public class GroupMenuServiceImpl implements GroupMenuService {
    @Autowired
    private GroupMenuDao groupMenuDao;
    @Override
    public void deleteGroupMenu(GroupMenu groupMenu) {
        if (groupMenu != null){
            groupMenuDao.deleteGroupMenu(groupMenu);
        }
    }

    @Override
    public void deleteGroupMenuByGroupId(Integer groupId) {
        GroupMenu groupMenu = new GroupMenu();
        groupMenu.setGroupId(groupId);
        deleteGroupMenu(groupMenu);
    }

    @Override
    public int insertGroupMenuBath(Integer groupId, Integer[] menuIds) {
        return groupMenuDao.insertGroupMenuBath(groupId, menuIds);
    }

    @Override
    public void deleteGroupMenuByMenuId(Integer menuId) {
        GroupMenu groupMenu = new GroupMenu();
        groupMenu.setMenuId(menuId);
        deleteGroupMenu(groupMenu);
    }
}
