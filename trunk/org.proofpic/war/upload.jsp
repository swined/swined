<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="upload.xsl"?>

<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<upload>
        <url><%= blobstoreService.createUploadUrl("/put") %></url>
</upload>
