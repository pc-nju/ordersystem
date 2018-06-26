package com.imooc.service.impl;

import com.imooc.bean.Action;
import com.imooc.bean.Menu;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dao.ActionDao;
import com.imooc.dto.ActionDto;
import com.imooc.dto.ResultDto;
import com.imooc.service.ActionService;
import com.imooc.service.GroupActionService;
import com.imooc.service.GroupMenuService;
import com.imooc.service.MenuService;
import com.imooc.util.CommonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/20 17:41
 */
@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private GroupActionService groupActionService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private GroupMenuService groupMenuService;

    @Override
    public ResultDto getActionWithId(Integer id) {
        Action action = new Action();
        action.setId(id);
        action = getAction(action);
        if (action == null){
            return new ResultDto().failure();
        }
        return new ResultDto().success().data(action);
    }

    @Override
    public ResultDto removeActionWithId(Integer id) {
        Action action = new Action();
        action.setId(id);
        action = getAction(action);
        if (action == null){
            return new ResultDto(PageCodeEnum.ACTION_NOT_EXISTS);
        }
        //删除Action的关联关系与Action
        removeRelationshipWithAction(action);
        return new ResultDto(PageCodeEnum.DELETE_SUCCESS);
    }

    @Override
    public ResultDto modifyAction(ActionDto actionDto) {
        if (CommonUtil.allIsBlank(actionDto.getName(), actionDto.getUrl(), actionDto.getMethod())){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        Action action = new Action();
        action.setId(actionDto.getId());
        if (getAction(action) == null){
            return new ResultDto(PageCodeEnum.ACTION_NOT_EXISTS);
        }
        BeanUtils.copyProperties(actionDto, action);
        //更新策略：name不能重复（由数据库控制）
        if (actionDao.updateAction(action) != 1){
            return new ResultDto(PageCodeEnum.UPDATE_FAILURE);
        }
        return new ResultDto(PageCodeEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultDto addAction(ActionDto actionDto) {
        if (CommonUtil.allIsBlank(actionDto.getName(), actionDto.getUrl(),
                actionDto.getMethod(), String.valueOf(actionDto.getMenuId()))){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        Action action = new Action();
        BeanUtils.copyProperties(actionDto, action);
        //插入策略：name不能重复（由数据库控制）
        if (actionDao.insertAction(action) != 1){
            return new ResultDto(PageCodeEnum.ADD_FAILURE);
        }
        return new ResultDto(PageCodeEnum.ADD_SUCCESS);
    }



    @Override
    public void deleteActionByMenuId(Integer menuId) {
        Action action = new Action();
        action.setMenuId(menuId);
        deleteAction(action);
    }

    @Override
    public int modifyActionMenuIdById(Integer actionId, Integer menuId) {
        Action action = new Action();
        action.setId(actionId);
        action.setMenuId(menuId);
        return updateAction(action);
    }



    /**
     * 获取指定Action
     */
    private Action getAction(Action action){
        List<Action> actionList = getActionList(action);
        if (CollectionUtils.isNotEmpty(actionList)){
            return actionList.get(0);
        }
        return null;
    }
    /**
     * 获取Action集合
     */
    private List<Action> getActionList(Action action){
        List<Action> actionList = new ArrayList<>();
        if (action != null){
            actionList = actionDao.selectActionList(action);
        }
        return actionList;
    }
    /**
     * 删除Action节点的关联关系（group/action）
     * 重难点：
     *     若菜单及其所有子菜单均无下辖的Action节点，则清除该父节点与用户组之间的关联关系（group/menu），
     *     若父菜单及其所下辖的其他子菜单均无下辖的Action节点，则清除该父菜单与用户组之间的关联关系（group/menu）
     */
    private void removeRelationshipWithAction(Action action){
        //删除Action节点的关联关系（group/action）
        groupActionService.deleteGroupActionByActionId(action.getId());
        //留着
        Integer menuId = action.getMenuId();
        //删除Action
        deleteAction(action);
        //若删除该action以后，父节点菜单及其所有子节点菜单均无其他下辖的Action节点，则清除该父节点与用户组之间的关联关系（group/menu）
        removeGroupMenuIfMenuEmpty(menuId);
    }


    /**
     * 删除Action
     */
    private boolean deleteAction(Action action){
        //这里的判断条件主要是面向按照Action的Id来删除Action来使用的
        return action != null && actionDao.deleteAction(action) == 1;
    }

    /**
     * 更新action
     * @param action 待更新条件
     * @return 受影响的行数
     */
    private int updateAction(Action action) {
        return actionDao.updateAction(action);
    }

    /**
     * 若菜单及其所有子菜单均无下辖的Action节点，则清除该父节点与用户组之间的关联关系（group/menu），
     * 若父菜单及其所下辖的其他子菜单均无下辖的Action节点，则清除该父菜单与用户组之间的关联关系（group/menu）
     * @param menuId 菜单id
     */
    private void removeGroupMenuIfMenuEmpty(Integer menuId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        if (CollectionUtils.isNotEmpty(menuService.getMenu(menu))){
            menu = menuService.getMenu(menu).get(0);

            //若菜单及其所有子菜单均无下辖的Action节点，则清除该父节点与用户组之间的关联关系（group/menu）
            removeGroupMenuIfSelfEmpty(menu);

            //清除父节点
            Menu parentMenu = new Menu();
            parentMenu.setId(menu.getParentId());
            if (CollectionUtils.isNotEmpty(menuService.getMenu(parentMenu))){
                //若父菜单及其所下辖的其他子菜单均无下辖的Action节点，则清除该父菜单与用户组之间的关联关系（group/menu）
                removeGroupMenuIfMenuEmpty(menuService.getMenu(parentMenu).get(0).getId());
            }
        }
    }
    /**
     *     若删除该action以后，父节点菜单无其他下辖的Action节点或下辖的所有子菜单，无Action节点，则清除该父节点与用户组
     * 之间的关联关系（group/menu）
     * @param menu Action节点所属的菜单节点
     *     重难点：所有子菜单无Action节点
     */
    private void removeGroupMenuIfSelfEmpty(Menu menu) {
        //自身及所有子菜单均无Action节点
        if (isAllActionListEmpty(menu)){
            //删除与用户组之间的关联关系（group/menu）
            groupMenuService.deleteGroupMenuByMenuId(menu.getId());
        }
    }

    /**
     * 菜单节点本身及其所下辖的所有子菜单节均无Action
     * @param menu 菜单节点
     * @return true:无 false：有
     */
    private boolean isAllActionListEmpty(Menu menu) {
        //下辖actionList不为空
        if (CollectionUtils.isNotEmpty(menu.getActionList())){
            return false;
        }
        //以该菜单节点为父节点的菜单节点actionList不为空
        Menu childMenu = new Menu();
        childMenu.setParentId(menu.getId());
        List<Menu> childMenuList = menuService.getMenu(childMenu);
        if (CollectionUtils.isNotEmpty(childMenuList)){
            for (Menu tempMenu : childMenuList){
                isAllActionListEmpty(tempMenu);
            }
        }
        return true;
    }
}
