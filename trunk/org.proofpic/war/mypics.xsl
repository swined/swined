<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
        <xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
        <xsl:template match='/mypics'>
			<html>
			    <head>
			        <title>My Pics</title>
			    </head>
			    <body>
			    	<xsl:apply-templates match="pic" />
			    </body>
			</html>
        </xsl:template>
        <xsl:template match='pic'>
        	<a href="http://{@key}.proofpic.org/">
        		<img src="http://{@key}.proofpic.org/?w=100&h=100" />
        	</a>
        </xsl:template>
</xsl:stylesheet>     
			