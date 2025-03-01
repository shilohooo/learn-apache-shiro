<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/23
  Time: 23:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
  <title>客户端首页</title>
</head>
<body>
<shiro:guest>
  欢迎游客访问，<a href="${pageContext.request.contextPath}/oauth2-login">点击登录</a><br/>
</shiro:guest>
<shiro:user>
  欢迎[<shiro:principal/>]登录<br/>
</shiro:user>
<shiro:hasRole name="admin">
  您有角色admin
</shiro:hasRole>
</body>
</html>
