/**
 * window.common || {}
 * 若有js插件的命名空间也叫common，则就用这个命名；否则，创一个common命名空间
 */
var common = window.common || {};

/**
 * 展示指定的消息内容。
 */
// common.showMessage = function(msg) {
// 	if(msg) {
// 		alert(msg);
// 	}
// }

/**
 * 对jQuery的ajax方法的二次封装
 */
common.ajax = function(param) {
	var mergeParam = $.extend({
		timeout : 10000
	} , param , {
		complete : function(response) {
			var url = response.getResponseHeader("url");
			if(url) {
				location.href = url;
			} else {
				if(param.complete && typeof param.complete === "function") {
					param.complete();
				}
			}
		}
	});
	$.ajax(mergeParam);
}

/**
 * 页面返回码定义，与后台PageCode定义对应
 */
common.pageCode = {
		"SUCCESS" : 1,
		"FAILURE" : 0,
		"ADD_SUCCESS" : 1000,
		"ADD_FAILURE" : 1001,
		"DELETE_SUCCESS" : 2000,
		"DELETE_FAILURE" : 2001,
		"UPDATE_SUCCESS" : 3000,
		"UPDATE_FAILURE" : 3001,
		"NOTHING_NEEDED_MODIFIED" : 5001,
		"USERNAME_EXIST" : 5002,
		"NICKNAME_EXIST" : 5003,
		"USER_NOT_EXISTS" : 5004,
		"PARAM_NULL" : 5005
}

common.menuPrefix = {
		"PREFIX_MENU" : "MENU_",
		"PREFIX_ACTION" : "ACTION_"
}

//展示指定消息
common.showMessage = function (msg) {
    /**
	 * if (msg)下的代码块执行的条件是：
	 * msg不为null 不为false 不为“”
     */
	if (msg){
		alert(msg);
	}
}
/**
 * 表单校验
 * 一、rules详解：
 * 1、单条检验规则：采用json格式，控件名：规则（规则见“messages_zh.js文件”）。例："title" : "required"
 * 2、多条检验规则：见下方“weight”，若规则中涉及到多个参数，比如“range”，那么数值以数组形式“[1,10]”
 *
 * 二、messages详解：
 *     规定某个控件无法通过校验时，会显示的提示信息。若这里为空，则显示默认提示信息。
 *
 * 三、自定义校验规则
 * $.validator.addMethod(name,method,message)
 * addMethod 的第一个参数：就是添加的验证方法的名字
 * addMethod 的第二个参数：是一个函数,接收三个参数(value,element,param) value 是元素的值,element是元素本身 param
 *                         是参数,这个比较重要,决定了用这个验证方法时的写法。如果只有一个参数,直接写,如果af:”a”,
 *                         那么a 就是这个唯一的参数,如果多个参数,用在[]里,用逗号分开
 * addMethod 的第三个参数：就是自定义的错误提示,这里的提示为:”必须是一个字母,且a-f”
 */
common.check = function () {
    $("#mainForm").validate({
        rules:{
            "title" : "required",
            "link" : {
                required : true,
                url : true
            },
            "weight" : {
                required : true,
                digits : true,
                range : [1,10],
                //自定义校验规则样例1的使用
                // notEqual : 9
            }
        },
        messages:{
        	"title" : "请输入标题",
            "weight" : {
                required : "此项必填",
                digits : "必须是数字",
                range : "数值范围在1-10之间",
            }
        }
    });
}

/**
 * 自定义校验规则
 * $.validator.addMethod(name,method,message)
 * addMethod 的第一个参数：就是添加的验证方法的名字
 * addMethod 的第二个参数：是一个函数,接收三个参数(value,element,param) value 是元素的值,element是元素本身 param
 *                         是参数,这个比较重要,决定了用这个验证方法时的写法。如果只有一个参数,直接写,如果af:”a”,
 *                         那么a 就是这个唯一的参数,如果多个参数,用在[]里,用逗号分开
 * addMethod 的第三个参数：就是自定义的错误提示,这里的提示为:”必须是一个字母,且a-f”
 */
//自定义校验规则样例1：
// $.validator.addMethod(
// 	"notEqual",
// 	function (value,element,param) {
// 		if (value !== param){
// 			return true;
// 		}
// 		return false;
//     },
// 	$.validator.format("输入的值不容许为{0}！")
// );
//自定义校验规则样例2：
// $.validator.addMethod("test",function(value,element,params){
//     if(value.length>1){
//         return false;
//     }
//     if(value>=params[0] && value<=params[1]){
//         return true;
//     }else{
//         return false;
//     }
// },"必须是一个字母,且a-f");

//校验商户表单
common.checkBusinessForm = function () {
    $("#mainForm").validate({
		rules:{
			"title":"required",
			"subtitle":"required",
			"city":"required",
			"category":"required",
			"price":{
				required:true,
				digits:true
			},
            "distance":{
                required:true,
                digits:true
            },
			"desc":{
                required:true,
				maxlength:200
			}
		},
		message:{
            "title" : "请输入标题"
		}
	});
}
//对jquery的ajax方法进行二次封装，增加超时时间和对Session超时的统一处理
common.ajax1 = function (param) {
	/*
	 * 利用Jquery的extend()方法，把传进来的参数，与我这里自定义的参数进行合并，意思是：
	 *     若外面（比如param就是用户传进来的参数）已规定超时时间，则覆盖里面自定义的“timeout”；
	 *     若未穿超时时间，则以自定义的超时时间为准
	 */
	var mergeParam = $.extend({
		timeout:10000
	}, param, {
		/*
		 * “complete”：只要ajax请求完毕就会调用，不管成功与否
		 *  问题：前面我们说了，若用户传过来的参数在我们自定义的变量之后（比如param在“timeout”之后），
		 *        若参数包含了我们这里扩展的变量，则会覆盖我们定义的变量。而我们自定义的“complete”又是在
		 *        param之后，所以所以若用户调用ajax的时候，也用了complete方法怎么办，那么则会被我们自定义的
		 *        complete方法覆盖。遇到这种问题怎么解决？比如：
		 *  common.ajax1({
		 *     ....
		 *     complete ; function(){
		 *         ....
		 *     }
		 *  });
		 *  解决办法：
		 *      首先判断Session是否超时，若超时，不论用户调不调用complete方法，都会跳转至登录页面
		 *      其次，若Session未超时，且用户传参中包括complete，并且complete是方法不是字段，
		 *      则执行用户调用的complete方法
		 */
		complete : function (response) {
			//Session超时
			if (response.getResponseHeader("SessionTimeoutPath")){
				//跳转到超时请求
				location.href = response.getResponseHeader("SessionTimeoutPath");
			} else {
				//若Session未超时，且用户传参中包括complete，且complete是方法不是字段
				if (param.complete && typeof param.complete === "function"){
					//执行用户调用的complete方法
					param.complete();
				}
			}
        }
	});
	$.ajax(mergeParam);
}