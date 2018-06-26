$(function() {
	common.showMessage($("#message").val());
});
function search(currentPage, searchKey) {
	$('#currentPage').val(currentPage);
    $('#searchKey').val(searchKey);
	$('#mainForm').submit();
}
function remove(id) {
	$("#_method").val("DELETE");
	$("#mainForm").attr("action", $("#basePath").val()+"/comment/"+id);
	$("#mainForm").submit();
}
