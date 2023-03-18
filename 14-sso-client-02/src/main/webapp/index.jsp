<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
  <title>首页</title>
</head>
<body>
<div>
  <shiro:guest>
    欢迎游客访问：<a href="login.jsp">登录</a>
  </shiro:guest>
</div>
<div>
  <shiro:user>
    欢迎[<shiro:principal/>]登录，<a href="logout">点击退出</a>
  </shiro:user>
</div>
</body>
</html>
