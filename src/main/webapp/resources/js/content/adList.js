$(function() {
	common.showMessage($("#message").val());
});

// function search(currentPage) {
// 	$("#currentPage").val(currentPage);
// 	$("#mainForm").submit();
// }

// function remove(id) {
// 	if(confirm("确定要删除这条广告吗？")) {
// 		$("#id").val(id);
// 		$("#mainForm").attr("action",$("#basePath").val() + "/ad/remove");
// 		$("#mainForm").submit();
// 	}
// }

// function modifyInit(id) {
// 	$("#id").val(id);
// 	$("#mainForm").attr("action",$("#basePath").val() + "/ad/modifyInit");
// 	$("#mainForm").submit();
// }
/*--------------------------我自己写的---------------------------*/
function search(currentPage,title) {
	$("#title").val(title);
	$("#currentPage").val(currentPage);
	$("#mainForm").submit();
}

//删除
function remove(id) {
	//设置提交路径
	$("#mainForm").attr("action", $("#basePath").val()+"/ad/"+ id +"/deletion");
	//提交
	$("#mainForm").submit();
}
//修改
function modifyInit(id) {
    //设置提交路径
    $("#mainForm").attr("action", $("#basePath").val()+"/ad/"+ id +"/modifyInit");
    //提交
    $("#mainForm").submit();
}
