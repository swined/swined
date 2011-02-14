<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<% out.print("<?xml-stylesheet type='text/xsl' href='/upload.xsl'?>"); %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>

<upload>
        <url><%= blobstoreService.createUploadUrl("/put") %></url>
</upload>
