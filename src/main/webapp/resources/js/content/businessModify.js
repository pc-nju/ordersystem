$(function () {
	common.showMessage($("#message").val());
	//验证表单
	common.checkBusinessForm();
});
function modify() {
	$("#mainForm").submit();
}
function goBack() {
	location.href = $("#basePath").val() + "/businesses";
}