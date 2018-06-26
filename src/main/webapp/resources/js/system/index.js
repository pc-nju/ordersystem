// 当前登录用户可以访问的菜单Map
// var menuMap = {};
//
// $(function() {
// 	common.ajax({
// 			url : $("#basePath").val() + "/session/menus",
// 			success : function(data) {
// 				if(data && data.length > 0) {
// 					$.each(data,function(i,value) {
// 						if(!menuMap[value.parentId]) {
// 							menuMap[value.parentId] = new Array();
// 						}
// 						menuMap[value.parentId].push(value);
// 					});
// 					initMenu();
// 				}
// 			}
// 	});
// });
//
// /**
//  * 初始化菜单
//  */
// function initMenu() {
// 	var menuList = menuMap[0];
// 	$("#menuDiv").html("");
// 	$.each(menuList,function(i,value) {
// 		$("#menuDiv").append("<li onclick='clickMenu(this," + value.id + ")'><a><span>" + value.name + "</span></a></li>");
// 	});
// }
//
// /**
//  * 根据父菜单ID初始化子菜单
//  */
// function initSubMenu(parentId) {
// 	var menuList = menuMap[parentId];
// 	$("#subMenuDiv").html("");
// 	$.each(menuList,function(i,value) {
// 		$("#subMenuDiv").append("<h3 onclick=\"clickSubMenu(this,'" + value.url + "')\"><a>" + value.name + "</a></h3>");
// 	});
// }
//
// /**
//  * 方法描述:单击菜单（页面上部菜单），初始化子菜单（即页面左部菜单）
//  */
// function clickMenu(element,id) {
// 	// 将同级节点的[选中样式]清空
// 	$("#menuDiv").children().attr("class","");
// 	// 将当前单击的节点置为[选中样式]
// 	$(element).attr("class","on");
// 	// 加载子菜单内容
// 	initSubMenu(id);
// }
//
// /**
//  * 方法描述:单击子菜单（页面左部菜单），初始化主页面
//  */
// function clickSubMenu(element,path) {
// 	// 将其他有[选中样式]的节点的样式清空
// 	$("#subMenuDiv").find(".on").attr("class","");
// 	// 将当前单击的节点置为[选中样式]
// 	$(element).children().attr("class","on");
// 	// 按指定地址加载主页面(iframe)
// 	$("#mainPage").attr("src",$("#basePath").val()+ path);
// }
//
// /**
// * 打开密码修改弹出层
// */
// function openAddDiv(){
// 	$("#mengban").css("visibility","visible");
// 	$(".wishlistBox").show();
// 	$(".wishlistBox").find(".persongRightTit").html("&nbsp;&nbsp;修改密码");
// 	$("#submitVal").show();
// }
//
// /**
// * 关闭密码修改弹出层
// */
// function closeDiv(){
// 	$("#mengban").css("visibility","hidden");
// 	$("#oldPassword").val("");
// 	$("#newPassword").val("");
// 	$("#newPasswordAgain").val("");
// 	$(".wishlistBox").hide();
// }
/*---------------------------------------------------------------------*/
/**
 * 方法描述:单击菜单（页面上部菜单），初始化子菜单（即页面左部菜单）
 */
// function clickFirstMenu(element) {
//
// 	//判断当前节点是否为【选中样式】，如果已经是【选中样式】，不再触发
// 	if ($(element).attr("class") !== "on"){
//         $("#menuDiv").children().attr("class","");
//         $(element).attr("class","on");
//         var basePath = $("#path").val();
//         if ($(element).find("span").html() === "内容管理"){
//             $("#subMenuDiv").html(
//             	"<h3 onclick=\"clickSecondMenu(this, '"+basePath+"/ad')\"><a>广告管理</a></h3>" +
// 				"<h3 onclick=\"clickSecondMenu(this, '"+ basePath +"/businesses')\"><a>商户管理</a></h3>" +
//                 "<h3 onclick=\"clickSecondMenu(this, '"+basePath+"/order')\"><a>订单管理</a></h3>" +
//                 "<h3 onclick=\"clickSecondMenu(this, '"+basePath+"/comment')\"><a>评论管理</a></h3>"
// 			);
//         } else if ($(element).find("span").html() === "系统管理"){
//             $("#subMenuDiv").html("<h3 onclick=\"clickSecondMenu(this, '"+ basePath + "/auth')\"><a>权限管理</a></h3>");
//         } else if ($(element).find("span").html() === "统计报表"){
//             $("#subMenuDiv").html("<h3 onclick=\"clickSecondMenu(this)\"><a>统计报表1</a></h3><h3 onclick=\"clickSecondMenu(this)\"><a>统计报表2</a></h3>");
//         }
//     }
// }
/**
 * 方法描述:单击子菜单（页面左部菜单），初始化主页面
 */
// function clickSecondMenu(element, path) {
// 	$("#subMenuDiv").find(".on").attr("class", "");
// 	$(element).children().attr("class", "on");
// 	//将主页跳转到指定路径
// 	$("#mainPage").attr("src", path);
// }
	//显示当前系统时间，并每隔1秒刷新一下
	window.onload = function () {
		setInterval(function () {
            var currentTime = new Date();
            var year = currentTime.getFullYear();
            var month = currentTime.getMonth() + 1;
            var date = currentTime.getDate();
            var hour = currentTime.getHours();
            var minute = currentTime.getMinutes();
            var second = currentTime.getSeconds();
            if (month < 10){
                month = "0" + month;
            }
            if (date < 10){
                date = "0" + date;
            }
            if (hour < 10){
                hour = "0" + hour;
            }
            if (minute < 10){
                minute = "0" + minute;
            }
            if (date < 10){
                second = "0" + second;
            }
            //填充当前时间
            $("#currentTime").text(year + "-" + month + "-" + date + " " + hour + "-" + minute + "-" + second);
        },1000);
    }
//创建一个全局变量，存放当前用户可以访问的Menu
var menuMap = {};
$(function () {
	common.ajax1({
		url : $("#basePath").val() + "/session/menus",
		success : function (data) {
			if (data.code === common.pageCode.SUCCESS && data.data.length > 0){
				var menus = data.data;
				$.each(menus, function (i, value) {
					if (!menuMap[value.parentId]){
						menuMap[value.parentId] = new Array();
					}
					menuMap[value.parentId].push(value);
                });
				initMenu();
			}
        }
	});
	    //加载修改密码表单的校验
        checkForm();
    }
);

/**
 * 初始化菜单
 */
function initMenu() {
    //所有的主菜单的父节点都是0
    var menuList = menuMap[0];
    $("#menuDiv").html("");
    $.each(menuList, function (i, value) {
        $("#menuDiv").append("<li onclick='clickFirstMenu(this," + value.id + ")'><a><span>" + value.name + "</span></a></li>");
    });
}

/**
 * 点击第一层菜单（主菜单）
 */
function clickFirstMenu(element, id) {
	//清空同级节点的样式
	$("#menuDiv").children().attr("class","");
	//将当前的节点样式改为选中
	$(element).attr("class","on");
	//初始化子菜单
	initSubMenu(id);
}

/**
 * 根据父菜单id初始化子菜单
 * @param menuId 父菜单id
 */
function initSubMenu(menuId) {
	//所有菜单的父节点都是0，所以只要取menuMap[0]，就取出所有了
	var menuList = menuMap[0];
	$.each(menuList, function (i,value) {
		if (value.id === menuId){
			var actionList = value.actionList;
			$("#subMenuDiv").html("");
            $.each(actionList, function (j, action) {
                $("#subMenuDiv").append("<h3 onclick=\"clickSecondMenu(this,'" + action.url + "')\"><a>" + action.name + "</a></h3>");
            })
		}
    })
}
function clickSecondMenu(element, path) {
	//找到当前被选中的子菜单，将其样式置空
	$("#subMenuDiv").find("class", ".on").attr("class", "");
	//将当前子菜单的样式设置为选中
	$(element).attr("class", ".on");
	//跳转页面
	$("#mainPage").attr("src", $("#basePath").val() + path);
}

//出现修改密码界面
function openAddDiv() {
	$("#mengban").show();
	$(".wishlistBox").show();
}
//修改密码
function modifyPassword() {
    if ($("#passwordEditForm").valid()){
        common.ajax1({
            url : $("#basePath").val() + "/sysUsers/"+ $("#userId").val() +"/password",
            type : "POST",
            data : {
                "_method" : "PUT",
                "oldPassword" : $.md5($("#oldPassword").val()),
                "newPassword" : $.md5($("#newPassword").val()),
                "newPasswordAgain" : $.md5($("#newPasswordAgain").val())
            },
            success : function (response) {
                if (response.code === common.pageCode.UPDATE_SUCCESS){
                    //关闭修改密码界面
                    closeDiv();
                }
                common.showMessage(response.msg);
            }
        });
    }
}
//关闭修改密码界面
function closeDiv() {
    $(".wishlistBox").hide();
    $("#mengban").hide();
}
//校验修改密码表单
function checkForm() {
    $("#passwordEditForm").validate({
        rules : {
            "oldPassword" : {
                required:true,
                rangelength:[4,20]
            },
            "newPassword" : {
                required:true,
                rangelength:[4,20]
            },
            "newPasswordAgain" : {
                required:true,
                rangelength:[4,20]
            }
        }
    });
}
