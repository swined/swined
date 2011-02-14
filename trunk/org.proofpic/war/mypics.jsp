<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml" %>
<% out.print("<?xml-stylesheet type='text/xsl' href='/mypics.xsl'?>"); %>
<%@ page import="org.proofpic.ImageInfo" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%
UserService userService = UserServiceFactory.getUserService();
String[] ids = ImageInfo.loadIds(userService.getCurrentUser());
%>

<mypics>
        <%
        	for (String id : ids)
        		out.println("<pic key='" + id + "'/>");
        %>
</mypics>
