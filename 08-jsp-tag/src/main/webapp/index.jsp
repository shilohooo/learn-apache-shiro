<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/5
  Time: 0:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 导入 shiro jsp 标签库 --%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%-- 导入自定义标签库 --%>
<%@taglib prefix="shiloh" tagdir="/WEB-INF/tags" %>
<html>
<head>
  <title>Index</title>
</head>
<body>
<div>
  <shiro:guest>
    欢迎游客访问，<a href="login.jsp">登录</a>
  </shiro:guest>
</div>
<%--
principal 标签用于显示用户身份信息，默认调用 Subject.getPrincipal(); 获取，即获取的是 PrimaryPrincipal
<shiro:principal type="java.lang.String"/>
 相当于 Subject.getPrincipals().oneByType(String.class);
<shiro:principal property="username"/>
相当于 ((User) Subject.getPrincipals()).getUsername()
--%>
<div>
  <shiro:user>
    欢迎[<shiro:principal/>]登录，<a href="logout">点击退出</a>
  </shiro:user>
</div>
<div>
  <shiro:authenticated>
    用户[<shiro:principal/>]已身份验证通过
  </shiro:authenticated>
</div>
<div>
  <shiro:notAuthenticated>
    未身份验证（包括记住我）
  </shiro:notAuthenticated>
</div>

<div>
  <shiro:hasRole name="admin">
    用户[<shiro:principal/>]拥有 admin 角色
  </shiro:hasRole>
</div>
<div>
  <shiro:hasAnyRoles name="admin, user">
    用户[<shiro:principal/>]拥有 admin / user 角色
  </shiro:hasAnyRoles>
</div>
<div>
  <shiro:lacksRole name="test">
    用户[<shiro:principal/>]没有 test 角色
  </shiro:lacksRole>
</div>

<div>
  <shiro:hasPermission name="user:create">
    用户[<shiro:principal/>]拥有 user:create 权限
  </shiro:hasPermission>
</div>
<div>
  <shiro:hasPermission name="user:view">
    用户[<shiro:principal/>]拥有 user:view 权限
  </shiro:hasPermission>
</div>
<div>
  <shiro:lacksPermission name="org:create">
    用户[<shiro:principal/>]没有 org:create 权限
  </shiro:lacksPermission>
</div>

<%-- 自定义标签库测试 --%>
<div>
  <shiloh:hasAllRoles name="admin, user">
    用户[<shiro:principal/>]拥有 admin 和 user 角色
  </shiloh:hasAllRoles>
</div>
<div>
  <shiloh:hasAllPermissions name="user:create, user:update">
    用户[<shiro:principal/>]拥有 user:create 和 user:update 权限
  </shiloh:hasAllPermissions>
</div>
<div>
  <shiloh:hasAnyPermissions name="user:create, abc:update">
    用户[<shiro:principal/>]拥有 user:create 或 abc:update 权限
  </shiloh:hasAnyPermissions>
</div>
</body>
</html>
