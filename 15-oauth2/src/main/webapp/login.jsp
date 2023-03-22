<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/21
  Time: 21:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
  <title>登录</title>
</head>
<body>
<div style="color: red">${error}</div>
<div>
  <form action="" method="post">
    <div>
      <label>
        用户名：
        <input type="text" name="username" value="<shiro:principal/>"/>
      </label>
    </div>
    <div>
      <label>
        密码：
        <input type="password" name="password"/>
      </label>
    </div>
    <div>
      <button type="submit">登录</button>
    </div>
  </form>
</div>
</body>
</html>
