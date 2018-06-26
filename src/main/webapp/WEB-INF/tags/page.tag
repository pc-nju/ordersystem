<!-- 设置编码 -->
<%@ tag language="java" pageEncoding="UTF-8" %>

<!-- 设置自定义属性：来接收使用这个标签传进来的分页参数 -->
<%@ attribute type="com.imooc.entity.Page" name="page" required="true" %>

<!-- 设置自定义属性：来接收使用这个标签传进来的函数名参数 -->
<%@ attribute type="java.lang.String" name="jsMethodName" required="true" %>

<%@ attribute type="java.lang.String" name="searchKey" required="true" %>

<!-- 设置自定义属性：来接收使用这个标签传进来的前缀参数（防止控件名重复） -->
<%@ attribute type="java.lang.String" name="prefix" required="true" %>

<script type="text/javascript">
    /**
     * window.common || {}
     * 若有js插件的命名空间也叫common，则就用这个命名；否则，创一个common命名空间
     * 问题：为什么要这么设置？
     * 原因：这样就避免了引入这个js文件的项目存在相同的方法名，导致不知道到底应该调用哪个方法！
     */
    var common = window.common || {};
    common.transferCurrentPage = function (currentPage,searchKey) {
        var rule = /^[0-9]*[1-9][0-9]*$/;
        if (!rule.test(currentPage)){
            currentPage = 1;
        }
//        $("#currentPage").val(currentPage);
//        $("#mainForm").submit();
        /**
         * $("#currentPage").val(currentPage);
         * $("#mainForm").submit();
         * 问题1：若有的跳转页面控件（"<input/>"）的id不是“currentPage”（id="currentPage"）怎么办？
         * 问题2：若请求方式是通过“ajax”异步请求，而不是通过form表单（$("#mainForm").submit()专用于form表单提交数据）怎么办？
         *
         * 问题解析：
         *     上述两个问题出现的原因是，我们不该把“设置控件的值”和“表单提交”设置死，而是由调用者来写这两部分逻辑，
         * 然后根据“调用者传过来的方法名”来调用“调用者写的这部分处理逻辑”
         *     重点：1、调用者写这部分逻辑；2、这里根据“函数名”和“这里处理好的参数”调用“调用者写的这部分逻辑”
         *     样例：“eval("${jsMethodName}(currentPage)")” 注意书写方式
         */
        eval("${jsMethodName}(currentPage,searchKey)");
    }
</script>

<!-- 分页 -->
<div class="page fix">
    <a href="javascript:common.transferCurrentPage('1','${searchKey}');" class="first">首页</a>
    <a href="javascript:common.transferCurrentPage('${page.currentPage - 1}','${searchKey}');" class="pre">上一页</a>
    当前第<span>${page.currentPage}/${page.totalPage}</span>
    <a href="javascript:common.transferCurrentPage('${page.currentPage + 1}','${searchKey}');" class="next">下一页</a>
    <a href="javascript:common.transferCurrentPage('${page.totalPage}','${searchKey}');" class="last">末页</a>
    跳至&nbsp;<input id="currentPageText" value="1" class="allInput w28" type="text"/>&nbsp;页&nbsp;
    <a href="javascript:common.transferCurrentPage($('#currentPageText').val(),'${searchKey}');" class="go">GO</a>
</div>