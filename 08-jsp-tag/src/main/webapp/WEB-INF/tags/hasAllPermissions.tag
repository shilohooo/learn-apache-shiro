<%-- 自定义标签：判断用户是否拥有指定权限的任意一种 --%>
<%@tag import="org.apache.commons.lang3.StringUtils" %>
<%@tag import="org.apache.shiro.SecurityUtils" %>
<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="name" type="java.lang.String" required="true" description="权限字符串列表" %>
<%@attribute name="delimiter" type="java.lang.String" required="false" description="权限字符串列表分隔符" %>
<%
  if (StringUtils.isBlank(delimiter)) {
    //  默认使用逗号作为分隔符
    delimiter = ",";
  }

  if (StringUtils.isBlank(name)) {
%>
<jsp:doBody/>
<%
    return;
  }

  final String[] roles = name.split(delimiter);
  if (!SecurityUtils.getSubject().isPermittedAll(roles)) {
    return;
  } else {
%>
<jsp:doBody/>
<%
  }
%>