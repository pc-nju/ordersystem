<%@ tag import="com.imooc.util.CommonUtil" %>
<!-- 设置编码 -->
<%@ tag language="java" pageEncoding="UTF-8" %>
<!-- 设置自定义属性：来接收使用这个标签传进来的函数名参数 -->
<%@ attribute type="java.lang.String" name="url" required="true" %>

<%@ attribute type="java.lang.String" name="method" required="true" %>

<%--好神奇，竟然凭空冒出来一个session--%>
<% if (CommonUtil.contain(session ,url, method)){%>
    <jsp:doBody/>
<%}%>
