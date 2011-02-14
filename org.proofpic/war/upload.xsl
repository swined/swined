<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
        <xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
        <xsl:template match='/upload'>
			<html>
			    <head>
			        <title>Upload</title>
			    </head>
			    <body>
			        <form action="{url}" method="post" enctype="multipart/form-data">
			            <input type="file" name="image"/>
			            <input type="submit" value="Submit"/>
			        </form>
			    </body>
			</html>
        </xsl:template>
</xsl:stylesheet>     
			