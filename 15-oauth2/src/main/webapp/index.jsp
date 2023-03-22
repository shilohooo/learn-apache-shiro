<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/21
  Time: 22:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>OAuth2集成</title>
</head>
<body>
<shiro:guest>
  游客您好，请<a href="${pageContext.request.contextPath}/login">登录</a>
</shiro:guest>
<shiro:user>
  欢迎[<shiro:principal/>]，点击<a href="${pageContext.request.contextPath}/logout">退出</a>
  <div>
    <a href="${pageContext.request.contextPath}/client" target="_blank">客户端管理</a>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user" target="_blank">用户管理</a>
  </div>
</shiro:user>
</body>
</html>
