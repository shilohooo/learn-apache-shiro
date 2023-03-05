<%--
  Created by IntelliJ IDEA.
  User: shiloh
  Date: 2023/3/5
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="java.io.Serializable" %>
<%@ page import="org.shiloh.dao.MySessionDAO" %>
<%@ page import="org.shiloh.session.OnlineSession" %>
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
<div>
  <shiro:user>
    <%--    测试获取 Session 中的属性--%>
    <%
      SecurityUtils.getSubject().getSession().setAttribute("key", "hello world");
      out.print(SecurityUtils.getSubject().getSession().getAttribute("key"));
    %>
    <br/>
    <%--      获取自定义的 Session，显示在线状态--%>
    <%
      final MySessionDAO mySessionDAO = new MySessionDAO();
      final Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
      final OnlineSession onlineSession = (OnlineSession) mySessionDAO.readSession(sessionId);
      out.print(onlineSession.getStatus().getInfo());
    %>
  </shiro:user>
</div>
</body>
</html>
