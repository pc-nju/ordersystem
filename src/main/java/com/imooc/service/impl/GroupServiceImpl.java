package com.imooc.service.impl;

import com.imooc.bean.Group;
import com.imooc.bean.GroupMenu;
import com.imooc.bean.SysUser;
import com.imooc.constant.PageCodeEnum;
import com.imooc.constant.SessionKeyConst;
import com.imooc.dao.*;
import com.imooc.dto.GroupDto;
import com.imooc.dto.ResultDto;
import com.imooc.service.GroupActionService;
import com.imooc.service.GroupMenuService;
import com.imooc.service.GroupService;
import com.imooc.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/19 11:17
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupActionService groupActionService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private GroupMenuService groupMenuService;
    @Autowired
    private HttpSession session;

    @Override
    public ResultDto getAllGroups() {
        Group group = new Group();
        List<GroupDto> groupList = getGroups(group);
        return new ResultDto().success().data(getGroups(group));
    }

    @Override
    public ResultDto getGroupWithId(Integer id) {
        Group group = new Group();
        group.setId(id);
        List<GroupDto> groupList = getGroups(group);
        if (CollectionUtils.isEmpty(groupList) || groupList.size() != 1 ){
            return new ResultDto().failure();
        }
        return new ResultDto().success().data(groupList.get(0));
    }

    @Override
    public ResultDto getGroupMenusWithId(Integer id) {
        Group group = groupDao.selectGroupMenusWithId(id);
        if (group == null ){
            return new ResultDto().failure();
        }
        return new ResultDto().success().data(wrapGroup(group));
    }

    @Override
    public ResultDto addGroup(Group group) {
        if (StringUtils.isBlank(group.getName())){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        if (groupDao.insertGroup(group) != 1){
            return new ResultDto(PageCodeEnum.ADD_FAILURE);
        }
        return new ResultDto(PageCodeEnum.ADD_SUCCESS);
    }

    @Override
    public ResultDto modifyGroup(Group group) {
        if (StringUtils.isBlank(group.getName())){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        if (groupDao.updateGroup(group) != 1){
            return new ResultDto(PageCodeEnum.UPDATE_FAILURE);
        }
        return new ResultDto(PageCodeEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultDto removeGroup(Group group) {
        //第一步：置空与该用户组相关联的用户的groupId
        setGroupIdNull(group.getId());
        //先删除与该用户组相关联的“用户组/菜单”和“用户组/操作”关联关系
        removeRelationWithGroup(group.getId());
        //再删除用户组
        if (groupDao.deleteGroup(group) != 1){
            return new ResultDto(PageCodeEnum.DELETE_FAILURE);
        }
        return new ResultDto(PageCodeEnum.DELETE_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResultDto assignMenusForGroup(GroupDto groupDto) {
        Integer[] menuIds = groupDto.getMenuIdList();
        Integer[] actionIds = groupDto.getActionIdList();
        Integer groupId = groupDto.getId();
        if (groupId == null) {
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        if (menuIds == null || menuIds.length <=0){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        if (actionIds == null || actionIds.length<= 0){
            return new ResultDto(PageCodeEnum.PARAM_NULL);
        }
        //删除该用户组以前已经分配的Menu和Action（先删再增，避免出现重复记录！！！！）
        groupMenuService.deleteGroupMenuByGroupId(groupId);
        groupActionService.deleteGroupActionByGroupId(groupId);

        int affectedMenuRows = groupMenuService.insertGroupMenuBath(groupId, menuIds);
        int affectedActionRows = groupActionService.insertGroupActionBatch(groupId, actionIds);
        if (affectedMenuRows != menuIds.length || affectedActionRows != actionIds.length){
            return new ResultDto(PageCodeEnum.ADD_FAILURE);
        }
        /*
         *     因为用户所能操作的所有菜单是放在Session中的，只要本次回话存在，Session就不会改变。而 当我们为用户重新分配
         * 了菜单的话，和Session中已存在的菜单是不一样的。为了使我们能立刻看到新分配的菜单，在我们为用户分配菜单成功以后，
         * 我们需要更新Session中用户能操作的菜单。
         */
        Group group = groupDao.selectGroupMenusInfoWithId(groupId);
        session.setAttribute(SessionKeyConst.SESSION_MENU_INFO, group.getMenuList());
        //因为并不一定该Menu下的所有Action都会分配给某个用户组，所以能操作的Action也要单独放到Session中
        session.setAttribute(SessionKeyConst.SESSION_ACTION_INFO, group.getActionList());
        return new ResultDto(PageCodeEnum.ADD_SUCCESS);
    }


    /**
     * 根据条件获取用户组集合
     * @param group 待查询条件对象
     * @return 用户组集合
     */
    private List<GroupDto> getGroups(Group group) {
        return wrapGroupList(groupDao.selectGroups(group));
    }

    private List<GroupDto> wrapGroupList(List<Group> groups) {
        List<GroupDto> groupDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groups)){
            for (Group group : groups){
                groupDtoList.add(wrapGroup(group));
            }
        }
        return groupDtoList;
    }

    private GroupDto wrapGroup(Group group) {
        GroupDto groupDto = new GroupDto();
        BeanUtils.copyProperties(group, groupDto);
        //初始化时，所有元素的父节点都是0
        groupDto.setParentId(0);
        return groupDto;
    }

    /**
     * 置空与该用户组相关联的用户的groupId
     * @param groupId 用户组id
     */
    private void setGroupIdNull(Integer groupId) {
        SysUser sysUser = new SysUser();
        sysUser.setGroupId(groupId);
        sysUserDao.updateGroupIdNull(sysUser);
    }
    /**
     * 根据用户组id，删除与该用户组相关联的“用户组/菜单”和“用户组/操作”关联关系
     * @param groupId 用户组id
     */
    private void removeRelationWithGroup(Integer groupId) {
        //先删除用户组与菜单(Menu)的关联关系
        groupMenuService.deleteGroupMenuByGroupId(groupId);
        //再删除用户组与操作（Action）的关联关系
        groupActionService.deleteGroupActionByGroupId(groupId);
    }
}
