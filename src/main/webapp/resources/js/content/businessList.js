$(function () {
	common.showMessage($("#message").val());
})
function remove(id) {
	if (confirm("是否要删除？")){
        //想要SpringMVC支持RestFul风格，前期准备工作参见CSDN
        $("#method").val("DELETE");
        $("#mainForm").attr("action",$("#basePath").val() + "/businesses/" + id);
        $("#mainForm").submit();
	}
}

function search(currentPage,searchKey) {
	$("#mainForm").attr("method","GET");
	$("#currentPage").val(currentPage);
	$("#searchKey").val(searchKey);
	// $("#mainForm").attr("action",$("#basePath").val() + "/businesses");
	$("#mainForm").submit();
}

function modifyInit(id) {
	location.href = $("#basePath").val() + "/businesses/" + id;
}