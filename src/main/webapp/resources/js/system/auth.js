$(function () {
	//屏蔽整个页面的浏览器右键菜单
	forbidRightClickMenu();
	//初始化用户树
	initUserTree1();
	//初始化用户组
	initGroupTree1();
	//初始化菜单树
	initMenuTree1();

});
/****************************通用 start*************************************/

//禁止页面除节点以外的地方右击
function forbidRightClickMenu() {
    $(document).bind("contextmenu",
        function(){
            return false;
        }
    );
}

//右击菜单显示的样式（在什么位置）
function rightClick1(event, rMenuId) {
    $("#" + rMenuId).css({
        "top" : event.clientY + "px",
        "left" : event.clientX + "px",
        "visibility" : "visible"
    });
}


/**
 * 关闭右键弹出的菜单
 */
function mouseDown() {
	//用户树右键菜单关闭
    $("#rMenu").css({
        "visibility" : "hidden"
    });
    //用户组树右键菜单关闭
    $("#groupMenu").css({
        "visibility" : "hidden"
    });
    //菜单树右键菜单关闭
    $("#menuMenu").css({
        "visibility" : "hidden"
    });
}

/********************** 单击鼠标，关闭右键弹出的菜单 start *********************************/
/**
 * 鼠标划出右键菜单层时，去除“鼠标经过菜单时”的样式。
 */
function divOut() {
    //鼠标划出右键菜单层时，开启“关闭右键弹出的菜单”事务（意思就是：鼠标划出右键菜单层时，单击鼠标，关闭右键弹出的菜单）
    $("body").bind("mousedown", mouseDown);

    $(".rMenuLiMove").addClass("rMenuLi");
    $(".rMenuLiMove").removeClass("rMenuLiMove");
}
/**
 * 鼠标在弹出层上方时，解除鼠标按下的事件
 */
function divOver() {
    //鼠标在弹出层上方时，禁止“关闭右键弹出的菜单”事务（意思就是：鼠标划出右键菜单层时，无法单击鼠标关闭右键弹出的菜单）
    $("body").unbind("mousedown", mouseDown);
}
/********************** 单击鼠标，关闭右键弹出的菜单 end *********************************/

/**
 * 鼠标在菜单间移动时样式的切换
 */
function move(element) {
    $(".rMenuLiMove").addClass("rMenuLi");
    $(".rMenuLiMove").removeClass("rMenuLiMove");

    $(element).addClass("rMenuLiMove");
    $(element).removeClass("rMenuLi");
}

/****************************通用 end*************************************/

/****************************用户树 start*************************************/
//初始化用户树
function initUserTree1() {
	common.ajax1({
		url : $("#basePath").val() + "/auth/users",
		success : function (data) {
			var setting = {
				view : {
					//双击展开
					dblClickExpand : true,
					showLine : true,
					//禁止多选
					selectedMulti : false
				},
				data : {
					simpleData : {
						//开启简单数据模式构建树
						enable : true,
						//因为默认的父节点字段是“pId”，所以需要改成后端返回的父节点字段
						pIdKey : "parentId"
					},
					key : {
						//显示的节点名称所对应的属性
						name : "nickName"
					}
				},
				callback : {
					//onClick：可以在这里手动定义函数，也可以在其他地方写函数，在这里写个函数名就行
					onClick : selectUser1,
                    onRightClick : userRightClick1
				}
			};
			//追加父节点
            data.push({id:0,nickName:"用户",open:true,isParent : true});
			$.fn.zTree.init($("#user"), setting, data);
        }
	});
}
//选中用户，并且异步加载其所属的用户组
function selectUser1(event, treeId, treeNode) {
	// var nodes = $.fn.zTree.getZTreeObj(treeId).getSelectedNodes();
	var nodes = $.fn.zTree.getZTreeObj(treeId).getSelectedNodes();
	var groupTree = $.fn.zTree.getZTreeObj("group");
	//先清空已经选中的选项
	var ckeckedNodes = groupTree.getCheckedNodes(true);
	if (ckeckedNodes.length > 0 ){
		//虽说是4个参数，但是可以省略
        groupTree.checkNode(ckeckedNodes[0], false);
	}
	//异步加载所属的用户组
	common.ajax1({
		url : $("#basePath").val() + "/auth/users/" + nodes[0].id + "/group",
		success : function (data) {
			//若当前的用户有用户组，则选中这个用户组
			if (data.groupId){
				groupTree.checkNode(groupTree.getNodeByParam("id", data.groupId), true);
			}
        }
	});
}

function userRightClick1(event, treeId, treeNode) {
	//若未选中节点右击，则直接返回
	if (!treeNode){
        return;
	}
	//显示右击菜单（平时是隐藏的）
	rightClick1(event, "rMenu");
	//根据是否是根节点，决定显示什么菜单
	if (treeNode.id === 0){
		//若是根节点，只显示新增；其他不显示
		$(".disabled").hide();
	} else {
        //若不是根节点，则都显示
        $(".disabled").show();
	}
	$.fn.zTree.getZTreeObj(treeId).selectNode(treeNode);
}

/****************************用户树 end*************************************/

/**************************用户数据维护部分 start**********************************/

// 初始化新增用户界面
function initAddUser() {
    //第一步：关闭右键弹出的菜单
    mouseDown();
    //第二步：清空用户维护界面的数据

    //第三步：显示蒙版
    $("#cover").show();
    //第四步：显示用户维护界面
    $("#userMaintain").show();
}

// 初始化修改用户界面
function initModifyUser(){
    //第一步：关闭右键弹出的菜单
    mouseDown();
    //第二步：将选中的节点信息显示出来
    var nodes = $.fn.zTree.getZTreeObj("user").getSelectedNodes();
	common.ajax1({
		url : $("#basePath").val() + "/auth/users/" +nodes[0].id,
		success : function (data) {
			$("#userId").val(data.id);
			$("#username").val(data.username);
			$("#nickName").val(data.nickName);
            //第三步：显示蒙版
            $("#cover").show();
            //第四步：显示用户维护界面
            $("#userMaintain").show();
        }
	});
}

//重置密码（将密码重置为与username相同）
function resetPassword() {
	mouseDown();
	var nodes = $.fn.zTree.getZTreeObj("user").getSelectedNodes();
	common.ajax1({
		url : $("#basePath").val() + "/auth/users/" + nodes[0].id,
		type : "POST",
		data : {
			"password" : $.md5(nodes[0].username),
			"_method" : "PUT"
		},
		success : function (data) {
			common.showMessage(data.msg);
        }
	});
}

//删除用户
function removeUser() {
	//第一步：关闭右击弹出菜单
	mouseDown();
	//第二步：找到被选中的节点
	var nodes = $.fn.zTree.getZTreeObj("user").getSelectedNodes();
    //第三步：发送异步请求
	common.ajax1({
		url : $("#basePath").val() + "/auth/users/" +nodes[0].id,
		type : "POST",
		data : {
			"_method" : "DELETE"
		},
		success : function (data) {
			if (data.code === common.pageCode.DELETE_SUCCESS){
                //第四步：若成功，重新加载用户树
				initUserTree1();
            }
            common.showMessage(data.msg);
        }
	});
}

/**
 * 保存用户信息，若主键存在，则修改；不存在则新增（将修改与新增用户合并到一个方法中）
 */
function saveUser() {
    //校验用户信息页面的表单数据
    $("#userForm").validate({
        rules : {
            "username" : "required",
            "nickName" : "required"
        }
    });
	if ($("#userForm").valid()){
		if ($("#userId").val()){
			//存在，修改
			common.ajax1({
				url : $("#basePath").val() + "/auth/users/" + $("#userId").val(),
				type : "POST",
				data : {
					"_method" : "PUT",
					"username" : $("#username").val(),
					"nickName" : $("#nickName").val()
				},
				success : function (data) {
					if (data.code === common.pageCode.UPDATE_SUCCESS){
						//再次初始化用户树，将修改后的用户显示出来
						initUserTree1();
						//关闭用户数据维护界面
						closeUser();
					}
					common.showMessage(data.msg);
                }
			});
		} else {
            //不存在，新增
			common.ajax1({
				url : $("#basePath").val() + "/auth/users",
				type : "POST",
				data : {
					"username" : $("#username").val(),
					"nickName" : $("#nickName").val()
				},
				success : function (data) {
					if (data.code === common.pageCode.ADD_SUCCESS){
						initUserTree1();
						closeUser();
					}
					common.showMessage(data.msg);
                }
			});
		}
	}
}

//关闭用户维护界面
function closeUser() {
	$("#userMaintain").hide();
	$("#cover").hide();
}

/**************************用户数据维护部分 end**********************************/


/**************************用户组数据维护部分 start**********************************/
//构建用户组树
function initGroupTree1() {
	//第一步：异步请求数据
	common.ajax1({
		url : $("#basePath").val() + "/auth/groups",
		success : function (data) {
			if (data.code === common.pageCode.SUCCESS){
                //第二步：设置树属性
                var setting = {
                    view : {
                        dbClickExpand : true,
                        showLine : true,
                        selectedMulti : false
                    },
                    check : {
                        enable : true,
                        chkStyle : "radio"

                    },
                    data : {
                        simpleData : {
                            enable : true,
                            pIdKey : "parentId"
                        },
                        key : {
                            //用哪个字段显示节点名称（其实默认就是“name”，我这里主要是提醒自己）
                            name : "name"
                        }
                    },
                    callback : {
                        onClick : selectGroup1,
                        onRightClick : groupRightClick1
                    }
                };
                //第三步：设置父节点
                data.data.push({
                    id : 0,
                    name : "用户组",
                    open : true,
					//根节点为父节点（这个必须设置，否则没有子节点时，外观和子节点一样，给人错觉）
                    isParent : true,
					//根节点节点不显示radioBox
					nocheck : true
                });
                //第四步：构建树
                $.fn.zTree.init($("#group"), setting, data.data);
			}
        }
	});
}

//单击选择用户组，出现关联的菜单项
function selectGroup1(event, treeId, treeNode, clickFlag) {
	//第一步：找到单击的节点
	var nodes = $.fn.zTree.getZTreeObj("group").getSelectedNodes(true);
	//若有节点被选中，且不是根节点
	if (nodes.length > 0 && nodes[0].id !== 0){
		//第二步：置空菜单树
		var menuTree = $.fn.zTree.getZTreeObj("menu");
		menuTree.checkAllNodes(false);

		//根据选中的节点，勾选关联的菜单树
		common.ajax1({
			url : $("#basePath").val() + "/auth/groups/" + nodes[0].id +"/menus",
			success : function (data) {
				if (data.code === common.pageCode.SUCCESS){
					//勾上关联的Menu
					for (var i=0; i<data.data.menuList.length; i++){
                        /**
						 * 首先，根据menu的ID，加上前缀，组装成"comboId"
						 * 然后，根据"comboId"，找到菜单节点
						 * 最后，勾选上
                         */
						menuTree.checkNode(menuTree.getNodeByParam("comboId",
							common.menuPrefix.PREFIX_MENU + data.data.menuList[i].id), true);
					}

					//勾上关联的Action
                    for (var j=0; j<data.data.actionList.length; j++){
                        /**
                         * 首先，根据menu的ID，加上前缀，组装成"comboId"
                         * 然后，根据"comboId"，找到菜单节点
                         * 最后，勾选上
                         */
                        menuTree.checkNode(menuTree.getNodeByParam("comboId",
                            common.menuPrefix.PREFIX_ACTION + data.data.actionList[j].id), true);
                    }
                    //勾选上单击的节点
                    $.fn.zTree.getZTreeObj("group").checkNode(treeNode,true);
				} else {
					common.showMessage(data.msg);
				}
            }
		});
	}

}

//右击节点，出现用户组的右击菜单
function groupRightClick1(event, treeId, treeNode) {
	//第一步：判断是否有节点别选中
	if (!treeNode){
		return;
	}
    //第二步：出现右击菜单容器
	rightClick1(event, "groupMenu");
    //第三步：判断是否是根节点
	if (treeNode.id === 0){
        //第四步：若是根节点，那么除“新增”子菜单，其他全部隐藏
		$(".disabled").hide();
	} else {
        //第四步：若不是根节点，则显示所有子菜单
        $(".disabled").show();
    }
    //第五步：选中右击节点
    $.fn.zTree.getZTreeObj(treeId).selectNode(treeNode);
}

/**
 * 初始化新增用户组界面
 */
function initAddGroup1() {
	//关闭右击菜单
	mouseDown();
    //修改界面标题
    $("#groupTitle").val("&nbsp;&nbsp;新增用户组");
	//出现蒙版
	$("#cover").show();
	//出现用户组维护界面
	$("#groupMaintain").show();
}

/**
 * 初始化修改用户组界面
 */
function modifyOfGroup1() {
    //关闭右击菜单
    mouseDown();
    //找到右击节点
	var nodes = $.fn.zTree.getZTreeObj("group").getSelectedNodes();
    //查询节点信息
    common.ajax1({
        url : $("#basePath").val() + "/auth/groups/" + nodes[0].id,
        success : function (data) {
            if (data.code === common.pageCode.SUCCESS){
            	//为维护界面的“groupId”和“groupName”赋值
            	$("#groupId").val(data.data.id);
            	$("#groupName").val(data.data.name);
            	//修改界面标题
				$("#groupTitle").val("&nbsp;&nbsp;修改用户组");
                //出现蒙版
                $("#cover").show();
                //出现用户组维护界面
                $("#groupMaintain").show();
            } else {
                common.showMessage(data.msg);
            }
        }
    });

}

/**
 * 删除用户组
 */
function removeGroup1() {
    //关闭右击菜单
    mouseDown();
    //找到右击节点
    var nodes = $.fn.zTree.getZTreeObj("group").getSelectedNodes();
    common.ajax1({
		url : $("#basePath").val() + "/auth/groups/" + nodes[0].id,
		type : "POST",
		data : {
			"_method" : "DELETE"
		},
		success : function (data) {
			if (data.code === common.pageCode.DELETE_SUCCESS){
				//若删除成功，刷新用户组树
				initGroupTree1();
			}
			common.showMessage(data.msg);
        }
	});
}

/**
 * 提交新增或修改的用户组
 */
function saveGroup1() {
	$("#groupForm").validate({
		rules : {
			"groupName" : "required"
		}
	});
	if ($("#groupForm").valid()){
        //通过隐藏域有无值来判断是新增还是修改（有：修改；无：新增）
		if ($("#groupId").val()){
			//若有值，修改
			common.ajax1({
                url : $("#basePath").val() + "/auth/groups/" + $("#groupId").val(),
                type : "POST",
                data : {
                	"_method" : "PUT",
                    "name" : $("#groupName").val()
                },
				success : function (data) {
                    if (data.code === common.pageCode.UPDATE_SUCCESS){
                        //若修改成功，则关闭用户组树维护界面
                        closeGroup1();
                        common.showMessage(data.msg);
                    } else {
                        common.showMessage(data.msg + "用户组名可能重复，请选择其他用户组名！");
					}
                }
			});
		} else {
			//若无值，新增
            common.ajax1({
                url : $("#basePath").val() + "/auth/groups",
                type : "POST",
                data : {
                    "name" : $("#groupName").val()
                },
                success : function (data) {
                    if (data.code === common.pageCode.ADD_SUCCESS){
                        //若新增成功，则关闭用户组树维护界面
                        closeGroup1();
                        common.showMessage(data.msg);
                    } else {
                        common.showMessage(data.msg + "用户组名可能重复，请选择其他用户组名！");
                    }
                }
            });
		}
	}
	//最后一步：刷新用户组树
	initGroupTree1();
}

/**
 * 关闭用户组维护界面
 */
function closeGroup1() {
    //关闭用户组维护界面
    $("#groupMaintain").hide();

    //关闭蒙版
    $("#cover").hide();
}



/**************************用户组数据维护部分 end**********************************/

/**************************菜单树部分 start**********************************/
function initMenuTree1() {
	common.ajax1({
		url : $("#basePath").val() + "/auth/menus",
		success : function (data) {
			if (data.code === common.pageCode.SUCCESS){
				var setting = {
                    view : {
                        //双击展开
                        dblClickExpand : true,
                        showLine : true,
                        //禁止多选
                        selectedMulti : false
                    },
                    check : {
                        enable : true
                    },
					edit : {
                        enable : true,
                    	drag : {
                            //默认拖拽节点是复制，改掉
                    		isCopy : false,
							inner : true,
							pre : false,
							next : false
						},
						//这两个默认为true，改为false
                        showRemoveBtn : false,
                        showRenameBtn : false
					},
                    data : {
                        simpleData : {
                            //开启简单数据模式构建树
                            enable : true,
                            //混合id，这里的id区分为带"MENU_"前缀的为菜单节点，带"ACTION_"前缀的为操作节点
							idKey : "comboId",
							//这里区分id带"MENU_"前缀的为父节点，带"ACTION_"前缀的为子节点
                            pIdKey : "comboParentId"
                        },
                        key : {
                            name : "name"
                        }
                    },
                    callback : {
                        beforeDrag : beforeDrag1,
                        beforeDrop : beforeDrop1,
                		onRightClick : onRightClick1
                    }
				};
				data.data.push({
					id : 0,
					//这是菜单项，加上菜单前缀
                    comboId : common.menuPrefix.PREFIX_MENU + "0",
					name : "菜单组",
					open : true,
					isParent : true,
                    nocheck:true
				});
				$.fn.zTree.init($("#menu"), setting, data.data);
			}
        }
	});
}

/**
 * 节点被拖拽之前的事件回调函数，
 * 返回false可以阻止拖拽
 */
function beforeDrag1(treeId, treeNodes) {
	//根节点不让拖拽
	return treeNodes[0].comboId !== common.menuPrefix.PREFIX_MENU + 0;
}
/**
 * 节点拖拽操作结束之前事件：
 * 将拖拽后的顺序提交，修改数据库中的顺序
 */
function beforeDrop1(treeId, treeNodes, targetNode, moveType, isCopy) {
    //所有节点都不能拖动到ACTION节点下(目标节点前缀是"ACTION_"，返回false)
	if (targetNode.comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0){
		return false;
	}
	//action节点不能拖动到根节点下
	if (treeNodes[0].comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0 &&
        targetNode.comboId.indexOf(common.menuPrefix.PREFIX_MENU + 0) === 0){
		return false;
	}
	//判断拖动节点的类型，不同类型的拖动节点，不同的处理方式
	var dropNodeType;
	if (treeNodes[0].comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0){
		dropNodeType = "action";
	} else {
        dropNodeType = "menu";
	}
    /*
	 * 两种情况：
	 * 第一种:拖动Action节点成为某个菜单节点的子节点（更换下menuId就行）
	 * 第二种：拖动Menu节点成为某个菜单节点的子节点（更换下ParentId,排在当前父节点最前）
     */
    common.ajax1({
		url : $("#basePath").val() + "/auth/menus/" + treeNodes[0].id + "/" + targetNode.id,
		type : "POST",
		data : {
			"_method" : "PUT",
			"dropNodeType" : dropNodeType
		},
		success : function (data) {
			if (data.code === common.pageCode.UPDATE_SUCCESS){
				//更新菜单树
				initMenuTree1();
			}
			common.showMessage(data.msg);
        }
	});

    return true;
}
/**
 * 在菜单树上右击显示右键菜单同时选中节点
 */
function onRightClick1(event, treeId, treeNode) {
	if (!treeNode){
		return;
	}
	//显示右击菜单容器（记住，是容器，里面的子菜单由后面的程序控制）
	rightClick1(event, "menuMenu");
    /**
	 * 决定显示哪些子菜单：
	 * 右击根节点：显示“新增菜单”
	 * 右击菜单节点：显示“新增菜单、新增动作、修改、删除”
	 * 右击ACTION节点：显示“修改、删除”
     */

    //首先显示所有子菜单
    $(".rMenuLi").show();

    //右击根节点：隐藏“新增动作、修改、删除”
	if (treeNode.comboId.indexOf(common.menuPrefix.PREFIX_MENU + 0) === 0){
		$(".disabled").hide();
	}
	//右击菜单节点：显示“新增菜单、新增动作、修改、删除”（已经显示所有子菜单了）

	//右击ACTION节点：隐藏“新增菜单、新增动作”
    if (treeNode.comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0){
        $(".menuClass").hide();
    }

    /**
     * 将当前节点的id,赋值给隐藏域“parentId”和“menu_id”:
     * 我们在新增菜单或新增Action的时候，需要知道是在哪个菜单节点下新增的，然后将那个菜单节点当成新增节点的父节点
     */
    $("#parentId").val(treeNode.id);
    $("#menu_id").val(treeNode.id);



    //选中右击节点
	$.fn.zTree.getZTreeObj(treeId).selectNode(treeNode);
}

/**
 * 初始化菜单维护页面
 */
function initAddMenu1() {
	//关闭右键菜单
	mouseDown();
	//设置界面标题，以区分新增还是修改
	$("#menuTitle").html("&nbsp;&nbsp;新增菜单");
	//出现蒙版
	$("#cover").show();
	//出现菜单维护界面
	$("#menuMaintain").show();
}
/**
 * 初始化动作维护页面
 */
function initAddAction1() {
    //关闭右键菜单
    mouseDown();
    //设置界面标题，以区分新增还是修改
    $("#actionTitle").html("&nbsp;&nbsp;新增动作");
    //出现蒙版
    $("#cover").show();
    //出现动作维护界面
    $("#actionMaintain").show();
}

/**
 * 初始化修改页面（菜单/动作）
 */
function modifyOfMenu1() {
    //关闭右键菜单
    mouseDown();

	var nodes = $.fn.zTree.getZTreeObj("menu").getSelectedNodes();

    if (nodes[0].comboId.indexOf(common.menuPrefix.PREFIX_MENU) === 0){
        //点击的是菜单，出现菜单维护界面
        initMenuModify(nodes);
	} else if (nodes[0].comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0){
        //点击的是动作，出现动作维护界面
        initActionModify(nodes);
	}
}

/**
 * 删除（菜单/动作）
 */
function removeOfMenu1() {
    //关闭右键菜单
    mouseDown();

    var nodes = $.fn.zTree.getZTreeObj("menu").getSelectedNodes();

    if (nodes[0].comboId.indexOf(common.menuPrefix.PREFIX_MENU) === 0){
    	if (confirm("该菜单下的所有动作都将会被删除，请确认是否删除？")){
            //点击的是菜单，删除菜单
            removeMenu1(nodes);
		}

    } else if (nodes[0].comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0){
        if (confirm("请确认是否删除？")){
            //点击的是动作，删除动作
            removeAction1(nodes);
        }
    }
}

/**
 * 初始化菜单维护界面
 */
function initMenuModify(nodes) {
    common.ajax1({
        url: $("#basePath").val() + "/auth/menus/" + nodes[0].id,
        success: function (data) {
            if (data.code === common.pageCode.SUCCESS) {
                //为维护界面控件赋值
                $("#menuId").val(data.data.id);
                $("#menuName").val(data.data.name);
                $("#url").val(data.data.url);

                //设置界面标题，以区分新增还是修改
                $("#menuTitle").html("&nbsp;&nbsp;修改菜单");
                //出现蒙版
                $("#cover").show();
                //出现动作维护界面
                $("#menuMaintain").show();
            }
        }
    });
}
/**
 * 初始化动作维护界面
 */
function initActionModify(nodes) {
    common.ajax1({
        url: $("#basePath").val() + "/auth/actions/" + nodes[0].id,
        success: function (data) {
            if (data.code === common.pageCode.SUCCESS) {
                //为维护界面控件赋值
                $("#actionId").val(data.data.id);
                $("#actionName").val(data.data.name);
                $("#actionUrl").val(data.data.url);
                $("#httpMethod").val(data.data.method);

                //设置界面标题，以区分新增还是修改
                $("#actionTitle").html("&nbsp;&nbsp;修改动作");
                //出现蒙版
                $("#cover").show();
                //出现动作维护界面
                $("#actionMaintain").show();
            }
        }
    });
}

/**
 * 删除菜单
 */
function removeMenu1(nodes) {
	common.ajax1({
		url : $("#basePath").val() + "/auth/menus/" + nodes[0].id,
		type : "POST",
		data : {
			"_method" : "DELETE"
		},
		success : function (data) {
			if (data.code === common.pageCode.DELETE_SUCCESS){
				//关闭菜单维护界面
				closeMenu1();
			}
			common.showMessage(data.msg);
        }
	});
}
/**
 * 删除操作
 */
function removeAction1(nodes) {
    common.ajax1({
        url : $("#basePath").val() + "/auth/actions/" + nodes[0].id,
        type : "POST",
        data : {
            "_method" : "DELETE"
        },
        success : function (data) {
            if (data.code === common.pageCode.DELETE_SUCCESS){
                //关闭操作维护界面
                closeAction1();
            }
            common.showMessage(data.msg);
        }
    });
}

/**
 * 提交菜单维护页面数据（根据"menuId"有无值，区分是新增还是修改）
 */
function saveMenu1() {
	$("#menuForm").validate({
        rules : {
            "menuName" : "required",
			"url" : "required"
        }
	});
	if ($("#menuForm").valid()){
        if ($("#menuId").val()){
            //修改
            modifyMenu1();
        } else {
            //新增
            addMenu1();
        }
	}
}

/**
 * 提交动作维护页面数据
 */
function saveAction1() {
    $("#actionForm").validate({
        rules : {
            "actionName" : "required",
            "actionUrl" : "required",
			"httpMethod" : "required"
        }
    });
    if ($("#actionForm").valid()){
        if ($("#actionId").val()){
            //修改
            modifyAction1();
        } else {
            //新增
            addAction1();
        }
	}
}
/**
 * 关闭菜单维护页面
 */
function closeMenu1() {
    //先关闭菜单维护界面
    $("#menuMaintain").hide();
    //再关闭蒙版
    $("#cover").hide();
    //刷新菜单树
    initMenuTree1();
}
/**
 * 关闭动作维护页面数据
 */
function closeAction1() {
    //先关闭动作维护界面
    $("#actionMaintain").hide();
	//再关闭蒙版
    $("#cover").hide();
    //刷新菜单树
	initMenuTree1();
}

/**
 * 修改菜单
 */
function modifyMenu1() {
    common.ajax1({
        url: $("#basePath").val() + "/auth/menus/" + $("#menuId").val(),
        type: "POST",
        data: {
            "_method": "PUT",
            "name": $("#menuName").val(),
            "url": $("#url").val()
        },
        success: function (data) {
            if (data.code === common.pageCode.UPDATE_SUCCESS) {
                //关闭菜单维护界面
                closeMenu1();
            }
            common.showMessage(data.msg);
        }
    });
}

/**
 * 新增菜单
 */
function addMenu1() {
    common.ajax1({
        url: $("#basePath").val() + "/auth/menus",
        type: "POST",
        data: {
            "name": $("#menuName").val(),
            "url": $("#url").val(),
			"parentId" : $("#parentId").val()
        },
        success: function (data) {
            if (data.code === common.pageCode.ADD_SUCCESS) {
                //关闭菜单维护界面
                closeMenu1();
            }
            common.showMessage(data.msg);
        }
    });
}
/**
 * 修改操作
 */
function modifyAction1() {
    common.ajax1({
        url: $("#basePath").val() + "/auth/actions/" + $("#actionId").val(),
        type: "POST",
        data: {
            "_method": "PUT",
            "name": $("#actionName").val(),
            "url": $("#actionUrl").val(),
            //传过去的是code
            "method" : $("#httpMethod").val()
        },
        success: function (data) {
            if (data.code === common.pageCode.UPDATE_SUCCESS) {
                //关闭动作维护界面
                closeAction1();
            }
            common.showMessage(data.msg);
        }
    });
}
/**
 * 新增操作
 */
function addAction1() {
    common.ajax1({
        url: $("#basePath").val() + "/auth/actions",
        type: "POST",
        data: {
            "name": $("#actionName").val(),
            "url": $("#actionUrl").val(),
			"menuId" : $("#menu_id").val(),
            //传过去的是code
            "method": $("#httpMethod").val()
        },
        success: function (data) {
            if (data.code === common.pageCode.ADD_SUCCESS) {
                //关闭动作维护界面
                closeAction1();
            }
            common.showMessage(data.msg);
        }
    });
}
/**************************菜单树部分 end**********************************/

/**
 * 为用户分配用户组
 */
function assignGroup1() {
    var userNodes = $.fn.zTree.getZTreeObj("user").getSelectedNodes(true);
    var groupNodes = $.fn.zTree.getZTreeObj("group").getCheckedNodes(true);
    if (userNodes.length <= 0){
        common.showMessage("未选中用户节点！");
        return;
    }
    if (userNodes[0].id === 0){
        common.showMessage("不能为根节点分配用户组！");
        return;
    }
    if (groupNodes.length <= 0){
        common.showMessage("未选中用户组！");
        return;
    }
    common.ajax1({
        url : $("#basePath").val() + "/auth/users/" + userNodes[0].id,
        type : "POST",
        data : {
            "_method" : "PUT",
            "groupId" : groupNodes[0].id
        },
        success : function (data) {
            if (data.code === common.pageCode.UPDATE_SUCCESS){
                common.showMessage("为用户分配用户组成功！");
            } else {
                common.showMessage("为用户分配用户组失败！");
            }
        }
    });
}
/**
 * 为用户组分配菜单
 */
function assignMenu1() {
    var groupNodes = $.fn.zTree.getZTreeObj("group").getCheckedNodes(true);
    var menuNodes = $.fn.zTree.getZTreeObj("menu").getCheckedNodes(true);
    if (groupNodes.length <= 0){
    	common.showMessage("请选中用户组节点！");
    	return;
	}
	if (menuNodes.length <= 0){
        common.showMessage("请选中菜单组节点！");
        return;
	}
	//提交的参数（即Menu节点的id和Action节点的id）。备注：虽说我们用comboId区分两个不同节点，但是本身各个节点的id后端是传过来的
	var param = {};

    //这是Menu节点的下标
    var m=0;
    //这是Action节点的下标
    var n=0;

    //构造json字符串
	for (var i=0; i<menuNodes.length; i++){
		//区分两种节点

		//Menu节点
		if (menuNodes[i].comboId.indexOf(common.menuPrefix.PREFIX_MENU) === 0){
			param["menuIdList["+ m +"]"] = menuNodes[i].id;
			m++;
		}

        //Action节点
        if (menuNodes[i].comboId.indexOf(common.menuPrefix.PREFIX_ACTION) === 0){
            param["actionIdList["+ n +"]"] = menuNodes[i].id;
            n++;
		}
	}
	common.ajax1({
		url : $("#basePath").val()+ "/auth/groups/" + groupNodes[0].id + "/menus",
		type : "POST",
		data : param,
		success : function (data) {
			if (data.code === common.pageCode.ADD_SUCCESS){
				common.showMessage("分配成功！");
			} else {
                common.showMessage("分配失败！");
			}
        }
	});

}