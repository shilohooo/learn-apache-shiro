<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login</title>
</head>
<body>
<div id="error" style="color: red">${errMsg}</div>
<form id="login-form" action="login" method="post">
  <div>
    <label>
      用户名：
      <input type="text" id="username" name="username" placeholder="请输入用户名"/>
    </label>
  </div>
  <div>
    <label>
      密码：
      <input type="password" id="password" name="password" placeholder="请输入密码"/>
    </label>
  </div>
  <div>
    <button id="login-btn" type="submit">登录</button>
  </div>
</form>
</body>
</html>