$(function() {
	common.showMessage($("#message").val());
	$("#mainForm").validate({
		rules : {
			"title" : "required",
			"link" : "required",
			"weight" : {
				required : true,
				digits : true,
				maxlength : 5
			}
		},
		messages : {
			"title" : "请输入标题！"
		}
	});
});

/***********************下面是我自己写的*****************************/
function modify() {
    $("#mainForm").submit();
}
function goback() {
    location.href = $('#basePath').val() + '/ad';
}