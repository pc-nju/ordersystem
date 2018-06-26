package com.imooc.service.impl;

import com.imooc.bean.GroupAction;
import com.imooc.dao.GroupActionDao;
import com.imooc.service.GroupActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 咸鱼
 * @date 2018/6/22 14:32
 */
@Service
public class GroupActionServiceImpl implements GroupActionService {
    @Autowired
    private GroupActionDao groupActionDao;
    @Override
    public void deleteGroupAction(GroupAction groupAction) {
        if (groupAction != null){
            groupActionDao.deleteGroupAction(groupAction);
        }
    }

    @Override
    public void deleteGroupActionByGroupId(Integer groupId) {
        GroupAction groupAction = new GroupAction();
        groupAction.setGroupId(groupId);
        deleteGroupAction(groupAction);
    }

    @Override
    public int insertGroupActionBatch(Integer groupId, Integer[] actionIds) {
        return groupActionDao.insertGroupActionBatch(groupId, actionIds);
    }

    @Override
    public void deleteGroupActionByMenuId(Integer menuId) {
        groupActionDao.deleteGroupActionByMenuId(menuId);
    }

    @Override
    public void deleteGroupActionByActionId(Integer actionId) {
        GroupAction groupAction = new GroupAction();
        groupAction.setActionId(actionId);
        groupActionDao.deleteGroupAction(groupAction);
    }
}
