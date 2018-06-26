package com.imooc.controller;

import com.imooc.bean.Group;
import com.imooc.bean.SysUser;
import com.imooc.constant.DictionaryConstant;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dto.*;
import com.imooc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2018/6/17 11:37
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ActionService actionService;



    @RequestMapping
    public String init(Model model){
        //將所有的HTTP动作查出來，放到jsp頁面
        model.addAttribute("httpMethodList",
                dictionaryService.getDictionaryListByType(DictionaryConstant.TYPE_DICTIONARY_HTTP_METHOD));
        return "/system/auth";
    }
    /***************************用户部分 start*************************************/

    /**
     * 获取所有用户
     */
    @RequestMapping("/users")
    @ResponseBody
    public List<SysUserAuthDto> getAllUsers(){
        return sysUserService.getAllUsers();
    }

    /**
     * 根据用户id获取该用户节点信息
     */
    @RequestMapping("/users/{id}")
    @ResponseBody
    public SysUserAuthDto getUserWithId(@PathVariable("id") Integer id){
        return sysUserService.getUserWithId(id);
    }

    /**
     * 根据用户id获取该用户关联的用户组
     */
    @RequestMapping("/users/{id}/group")
    @ResponseBody
    public SysUserAuthDto getUserGroupWithUserId(@PathVariable("id") Integer id){
        return sysUserService.getUserWithId(id);
    }

    /**
     * 新增用户
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto addSysUser(SysUser sysUser){
        ResultDto resultDto;
        if (sysUserService.addUser(sysUser)){
            resultDto = new ResultDto(PageCodeEnum.ADD_SUCCESS);
        } else {
            resultDto = new ResultDto(PageCodeEnum.ADD_FAILURE_SYS_USER);
        }
        return resultDto;
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultDto modifySysUser(SysUser sysUser){
        ResultDto resultDto;
        if (sysUserService.modifyUser(sysUser)){
            resultDto = new ResultDto(PageCodeEnum.UPDATE_SUCCESS);
        } else {
            resultDto = new ResultDto(PageCodeEnum.UPDATE_FAILURE_SYS_USER);
        }
        return resultDto;
    }
    /**
     * 删除用户
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public PageCodeDto deleteSysUser(@PathVariable("id") Integer id){
        return sysUserService.deleteSysUser(id);
    }

    /***************************用户部分 end*************************************/

    /***************************用户组部分 start*************************************/

    /**
     * 获取所有用户组
     */
    @RequestMapping("/groups")
    @ResponseBody
    public ResultDto getAllGroups(){
        return groupService.getAllGroups();
    }
    /**
     * 根据用户组id获取该用户组节点信息
     */
    @RequestMapping("/groups/{id}")
    @ResponseBody
    public ResultDto getGroupWithId(@PathVariable("id") Integer id){
        return groupService.getGroupWithId(id);
    }

    /**
     * 增加用户组
     */
    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto addGroup(Group group){
        return groupService.addGroup(group);
    }
    /**
     * 修改用户组
     */
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultDto modifyGroup(Group group){
        return groupService.modifyGroup(group);
    }
    /**
     * 根据用户组id获取该用户组下的所有关联菜单
     */
    @RequestMapping("/groups/{id}/menus")
    @ResponseBody
    public ResultDto getGroupMenusWithId(@PathVariable("id") Integer id){
        return groupService.getGroupMenusWithId(id);
    }
    /**
     * 删除用户组
     */
    @RequestMapping(value = "/groups/{id}/menus", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultDto removeGroup(Group group){
        return groupService.removeGroup(group);
    }
    /**
     * 为用户组分配菜单
     */
    @RequestMapping(value = "/groups/{id}/menus", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto assignMenusForGroup(GroupDto groupDto){
        return groupService.assignMenusForGroup(groupDto);
    }

    /***************************用户组部分 end*************************************/

    /***************************菜单树部分 start*************************************/

    /***************************菜单部分 start*************************************/
    /**
     * 获取所有菜单树节点
     */
    @RequestMapping("/menus")
    @ResponseBody
    public ResultDto getAllMenus(){
        return menuService.getAllMenus();
    }
    /**
     * 根据id获取指定菜单
     */
    @RequestMapping("/menus/{id}")
    @ResponseBody
    public ResultDto getMenuWithId(@PathVariable("id") Integer id){
        return menuService.getMenuWithId(id);
    }
    /**
     * 根据id删除指定菜单
     */
    @RequestMapping(value = "/menus/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultDto removeMenuWithId(@PathVariable("id") Integer id){
        return menuService.removeMenuWithId(id);
    }
    /**
     * 修改指定菜单
     */
    @RequestMapping(value = "/menus/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultDto modifyMenu(MenuDto menuDto){
        return menuService.modifyMenu(menuDto);
    }
    /**
     * 新增菜单
     */
    @RequestMapping(value = "/menus", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto addMenu(MenuDto menuDto){
        return menuService.addMenu(menuDto);
    }
    /***************************菜单部分 end*************************************/

    /***************************操作部分 start*************************************/
    /**
     * 根据id获取指定Action
     */
    @RequestMapping("/actions/{id}")
    @ResponseBody
    public ResultDto getActionWithId(@PathVariable("id") Integer id){
        return actionService.getActionWithId(id);
    }
    /**
     * 根据id删除指定Action
     */
    @RequestMapping(value = "/actions/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultDto removeActionWithId(@PathVariable("id") Integer id){
        return actionService.removeActionWithId(id);
    }
    /**
     * 修改指定Action
     */
    @RequestMapping(value = "/actions/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultDto modifyAction(ActionDto actionDto){
        return actionService.modifyAction(actionDto);
    }
    /**
     * 新增Action
     */
    @RequestMapping(value = "/actions", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto addAction(ActionDto actionDto){
        return actionService.addAction(actionDto);
    }
    /***************************操作部分 end*************************************/

    /**
     * 拖动节点成为某个菜单的子节点
     */
    @RequestMapping(value = "/menus/{dropNodeId}/{targetNodeId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResultDto dropNode(MoveNodeForZtreeDto moveNodeForZtreeDto){
        return menuService.dropNode(moveNodeForZtreeDto);
    }

    /***************************菜单树部分 end*************************************/
}
