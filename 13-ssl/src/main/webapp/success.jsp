<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/12
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<html>
<head>
  <title>权限测试</title>
</head>
<body>
<shiro:hasRole name="admin">
  <shiro:principal/>拥有 admin 角色
</shiro:hasRole>
</body>
</html>
