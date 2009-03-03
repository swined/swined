<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='note'>
	</xsl:template match='note'>
	<xsl:template match='/nb'>
		<html>
			<head>
				<title>notes</title>
				<style>
					DIV {
						border: 1px solid gray;
					}
				</style>
			</head>
			<body>
				<div class='tags'>
				</div>
				<div class='notes'>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
