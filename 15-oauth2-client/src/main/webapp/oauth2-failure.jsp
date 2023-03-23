<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/23
  Time: 23:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>登录失败</title>
</head>
<body>
OAuth2登录失败了，如错误的auth code。<br/>
<c:if test="${not empty param.error}">
  错误码：
  ${param.error}
  错误信息：
  ${param.errorDesc}
</c:if>
</body>
</html>
