<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/5
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>登录</title>
</head>
<body>
<div>
  <div style="color: red;">${shiroLoginFailure}</div>
  <div style="color: red;">${error}</div>
  <form action="" method="post">
    <div>
      <label for="username">
        用户名：
        <input type="text" id="username" name="username" placeholder="请输入用户名"/>
      </label>
    </div>
    <div>
      <label for="password">
        密码：
        <input type="password" id="password" name="password" placeholder="请输入密码"/>
      </label>
    </div>
    <div>
      <label for="rememberMe">
        记住我：
        <input type="checkbox" id="rememberMe" name="rememberMe"/>
      </label>
    </div>
    <div>
      <button type="submit">登录</button>
    </div>
  </form>
</div>
</body>
</html>
