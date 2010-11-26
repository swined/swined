<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>


<html>
    <head>
        <title>Upload</title>
    </head>
    <body>
        <form action="<%= blobstoreService.createUploadUrl("/put") %>" method="post" enctype="multipart/form-data">
            <input type="file" name="image">
            <input type="submit" value="Submit">
        </form>
    </body>
</html>