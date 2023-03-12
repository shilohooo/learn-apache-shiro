<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/12
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>无权限访问</title>
</head>
<style>
  .error {
    color: red;
  }
</style>
<body>
<div class="error">
  <h1>您没有权限访问指定资源:(</h1>
  <div>${exception.message}</div>
</div>
</body>
</html>
