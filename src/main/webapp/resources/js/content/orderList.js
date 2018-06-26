$(function() {
	common.showMessage($("#message").val());
});
function search(currentPage, searchKey) {
	$('#searchKey').val(searchKey);
	$('#currentPage').val(currentPage);
	$('#mainForm').submit();
}
