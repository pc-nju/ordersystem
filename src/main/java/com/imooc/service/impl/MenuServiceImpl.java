package com.imooc.service.impl;

import com.imooc.bean.Action;
import com.imooc.bean.Menu;
import com.imooc.constant.MenuConst;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dao.ActionDao;
import com.imooc.dao.GroupMenuDao;
import com.imooc.dao.MenuDao;
import com.imooc.dto.MenuDto;
import com.imooc.dto.MoveNodeForZtreeDto;
import com.imooc.dto.NodeForZtreeDto;
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
 * @date 2018/6/20 17:26
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ActionService actionService;
    @Autowired
    private GroupMenuService groupMenuService;
    @Autowired
    private GroupActionService groupActionService;

    @Override
    public ResultDto getAllMenus() {
        Menu menu = new Menu();
        ResultDto resultDto;
        if (CollectionUtils.isNotEmpty(getMenuTree(menu))){
            resultDto = new ResultDto().success().data(getMenuTree(menu));
        } else {
            resultDto = new ResultDto().failure();
        }
        return resultDto;
    }

    @Override
    public ResultDto getMenuWithId(Integer id) {
        Menu menu = menuDao.selectMenuWithId(id);
        if (menu == null){
            return new ResultDto().failure();
        }
        MenuDto menuDto = wrapMenu(menu);
        return new ResultDto().success().data(menuDto);
    }

    /**
     * 删除策略：菜单节点下的Action节点全部删除，Menu节点的父节点变成根节点
     * 重难点：删除以该菜单节点为父节点的菜单、这些菜单的子菜单以及操作节点，及其这些节点的关联关系
     * 分析：看起来很复杂，其实我们只要以一个菜单项为模板，删除该菜单的相关所有数据，然后找出所有的
     *       关联菜单，调用这个模板即可（核心思想就是递归调用）
     */
    @Override
    public ResultDto removeMenuWithId(Integer id) {
        Menu menu = menuDao.selectMenuWithId(id);
        if (menu == null){
            return new ResultDto(PageCodeEnum.MENU_NOT_EXISTS);
        }
        //首先删除Menu的关联关系
        removeMenuRelationShip(id);

        //删除所有以该菜单节点为父节点的菜单及其关联关系（其实就是一个循环调用这个方法）
        Menu childMenu = new Menu();
        childMenu.setParentId(id);
        List<Menu> menuList = menuDao.selectMenu(childMenu);
        if (CollectionUtils.isNotEmpty(menuList)){
            for (Menu tempMenu : menuList){
                removeMenuWithId(tempMenu.getId());
            }
        }
        //删除Menu本身
        deleteMenu(menu);
        return new ResultDto(PageCodeEnum.DELETE_SUCCESS);
    }

    @Override
    public ResultDto modifyMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        if (getMenuWithId(menuDto.getId()) == null){
            return new ResultDto(PageCodeEnum.MENU_NOT_EXISTS);
        }
        BeanUtils.copyProperties(menuDto, menu);
        return updateMenu(menu);
    }

    @Override
    public ResultDto addMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDto, menu);
        return insertMenu(menu);
    }

    @Override
    public ResultDto dropNode(MoveNodeForZtreeDto moveNodeForZtreeDto) {
        Integer targetNodeId = moveNodeForZtreeDto.getTargetNodeId();
        Integer dropNodeId = moveNodeForZtreeDto.getDropNodeId();
        String dropNodeType = moveNodeForZtreeDto.getDropNodeType();

        //首先查目标节点存不存在（目标节点是菜单节点）
        Menu menu = getMenuById(targetNodeId);
        if (menu == null){
            return new ResultDto(PageCodeEnum.TARGET_MENU_NOT_EXISTS);
        }
        if (MenuConst.TYPE_MENU.equals(dropNodeType)){
            //若被拖动的节点是菜单节点：先更改parentId，再修改orderNum（排序）

            //首先查看被拖动节点（菜单节点）存不存在
            Menu dropMenu = getMenuById(dropNodeId);
            if (dropMenu == null){
                return new ResultDto(PageCodeEnum.DROP_MENU_NOT_EXISTS);
            }

            //更新parentId，更改orderNum（排序）
            if (updateDropMenu(dropNodeId, targetNodeId) != 1){
                return new ResultDto(PageCodeEnum.UPDATE_FAILURE);
            }
        } else if (MenuConst.TYPE_ACTION.equals(dropNodeType)){
            //若被拖动的节点是Action节点：更改menuId即可

            /*
             * 首先查看被拖动节点（Action节点）存不存在
             * 备注：我一直在想一个问题，那就是状态码放在Controller层，还是放在Service层，而在这里我要
             *       复用service的时候发现，因为状态码的存在，反而把我复用的过程弄复杂了，但是我现在想
             *       不到什么好的解决办法，去解决返回多个状态码的问题。
             * 初步想法：判断过程全部交给Controller，Service只留下具体的业务
             */
            Action action = (Action) actionService.getActionWithId(dropNodeId).getData();
            if (action == null){
                return new ResultDto(PageCodeEnum.ACTION_NOT_EXISTS);
            }
            if (actionService.modifyActionMenuIdById(dropNodeId, targetNodeId) != 1){
                return new ResultDto(PageCodeEnum.UPDATE_FAILURE);
            }
        }
        return new ResultDto(PageCodeEnum.UPDATE_SUCCESS);
    }

    @Override
    public List<Menu> getMenu(Menu menu) {
        return getMenuList(menu);
    }

    /**
     * 获取Menu集合
     * @param menu 条件
     * @return Menu集合
     */
    private List<Menu> getMenuList(Menu menu) {
        return menuDao.selectMenu(menu);
    }

    private List<NodeForZtreeDto> getMenuTree(Menu menu) {
        return wrapMenuTreeList(menuDao.selectMenu(menu));
    }

    private List<NodeForZtreeDto> wrapMenuTreeList(List<Menu> menus) {
        List<NodeForZtreeDto> nodeForZtreeDtoList = new ArrayList<>();
        for (Menu menu : menus){
            List<NodeForZtreeDto> nodeList = wrapMenuTree(menu);
            if (CollectionUtils.isNotEmpty(nodeList)){
                nodeForZtreeDtoList.addAll(nodeList);
            }
        }
        return nodeForZtreeDtoList;
    }

    /**
     * 一个Menu节点能拆分成n个“NodeForZtreeDto”节点（1个menu节点 + n个action节点，每个节点都对应一个NodeForZtreeDto）
     * @param menu 菜单
     * @return NodeForZtreeDto集合
     */
    private List<NodeForZtreeDto> wrapMenuTree(Menu menu) {
        List<NodeForZtreeDto> nodeList = new ArrayList<>();

        NodeForZtreeDto menuNode = new NodeForZtreeDto();
        menuNode.setId(menu.getId());
        menuNode.setName(menu.getName());
        //menu的id和parentId都需要加"MENU_"前缀
        menuNode.setComboId(NodeForZtreeDto.PREFIX_MENU + menu.getId());
        menuNode.setComboParentId(NodeForZtreeDto.PREFIX_MENU + menu.getParentId());
        //菜单节点需要展开
        menuNode.setOpen(true);
        menuNode.setIsParent("true");
        nodeList.add(menuNode);

        List<Action> actionList = menu.getActionList();
        if (CollectionUtils.isNotEmpty(actionList)){
            for (Action action : actionList){
                NodeForZtreeDto actionNode = new NodeForZtreeDto();
                actionNode.setId(action.getId());
                actionNode.setName(action.getName());
                //action的id的前缀是"ACTION_"
                actionNode.setComboId(NodeForZtreeDto.PREFIX_ACTION + action.getId());
                //action的父节点就是menuId（action必属于一个menu），所以前缀是"MENU_"
                actionNode.setComboParentId(NodeForZtreeDto.PREFIX_MENU + action.getMenuId());
                //Action节点不需要展开
                actionNode.setOpen(false);
                //Action节点不是父节点
                actionNode.setIsParent("false");
                nodeList.add(actionNode);
            }
        }
        return nodeList;
    }

    private MenuDto wrapMenu(Menu menu){
        MenuDto menuDto = new MenuDto();
        menuDto.setId(menu.getId());
        menuDto.setName(menu.getName());
        menuDto.setUrl(menu.getUrl());
        return menuDto;
    }

    /**
     * 根据菜单Id，删除与该菜单有关的关联关系（删除策略是：删除了菜单节点，其下的所有节点归到根节点下）
     * @param menuId 菜单Id
     */
    private void removeMenuRelationShip(Integer menuId) {
        //删除与Group的关联关系
        groupMenuService.deleteGroupMenuByMenuId(menuId);
        //删除以该菜单节点为父节点的所有Action节点与Group的关联关系
        groupActionService.deleteGroupActionByMenuId(menuId);
        //删除以该菜单节点为父节点的所有Action节点
        actionService.deleteActionByMenuId(menuId);
    }

    /**
     * 删除Menu
     * @param menu 待删除条件
     */
    private void deleteMenu(Menu menu) {
        if (menu != null){
            menuDao.deleteMenu(menu);
        }
    }

    /**
     * 更新菜单
     * @param menu 待更新条件
     * @return 状态码
     */
    private ResultDto updateMenu(Menu menu) {
        //name和url成员变量不能同时为空或""
        if (CommonUtil.allIsBlank(menu.getName(), menu.getUrl())){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        //修改的逻辑：除自身以外，菜单的name在数据库里不能有重复（由数据库实现）
        if (menuDao.updateMenu(menu) != 1){
            return new ResultDto(PageCodeEnum.UPDATE_FAILURE);
        }
        return new ResultDto(PageCodeEnum.UPDATE_SUCCESS);
    }

    /**
     * 新增菜单
     * @param menu 待新增菜单
     * @return 状态码
     */
    private ResultDto insertMenu(Menu menu) {
        //name和url成员变量不能同时为空或""
        if (CommonUtil.allIsBlank(menu.getName(), menu.getUrl())){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        //新增的逻辑：菜单的name在数据库里不能有重复，且排序总在根节点的第1位（由数据库实现）
        if (menuDao.insertMenu(menu) != 1){
            return new ResultDto(PageCodeEnum.ADD_FAILURE);
        }
        return new ResultDto(PageCodeEnum.ADD_SUCCESS);
    }

    /**
     * 根据菜单id获取菜单
     * @param menuId 菜单id
     * @return 菜单
     */
    private Menu getMenuById(Integer menuId){
        return menuDao.selectMenuWithId(menuId);
    }

    /**
     *     根据菜单id，更新其parentId，同时将该菜单的orderNum(排序值)设置为父菜单（parentId）下所有的子菜单的
     * orderNum(排序值)+1，保证排在父节点下的第一位
     * @param menuId 菜单id
     * @param parentId 父菜单id
     * @return 受影响的行数
     */
    private int updateDropMenu(Integer menuId, Integer parentId) {
        Menu menu = new Menu();
        menu.setId(menuId);
        menu.setParentId(parentId);
        return menuDao.updateDropMenu(menu);
    }
}
