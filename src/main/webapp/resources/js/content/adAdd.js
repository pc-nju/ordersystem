$(function() {
    common.showMessage($("#message").val());
    //调用表单校验
    common.check();
});

/***********************下面是我自己写的*****************************/
function add() {
    $("#mainForm").submit();
}
function goback() {
    location.href = $('#basePath').val() + '/ad';
}