<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.prooflink.Link" %>
<%@ page import="org.prooflink.DomainUtils" %>

<%    
	String id = DomainUtils.guessSubdomain(request);
	Link link = null;		
	try {
		link = Link.load(id);
	} catch (Throwable e) {
		response.sendRedirect("/404.jsp?link=" + id);
	}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= id %></title>
</head>
<body>
<center>
<a href='http://<%= request.getServerName() %>'><%= request.getServerName() %></a> =&gt; <a href='<%= link.getLink() %>'><%= link.getLink() %></a>
<br/>
added by <%= link.getOwner().getNickname() %>
</center>
</body>
</html>