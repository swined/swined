<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='/nb/note'>
		<div class='note'><xsl:value-of select='text'/></div>
	</xsl:template>
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
					<xsl:apply-templates select='note' />
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
