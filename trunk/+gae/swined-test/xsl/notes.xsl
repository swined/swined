<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='/nb/tags/tag'>
		<xsl:value-of select='.'/><br/>
	</xsl:template>
	<xsl:template match='/nb/note'>
		<div class='note'><xsl:value-of select='text'/></div>
	</xsl:template>
	<xsl:template match='/nb'>
		<html>
			<head>
				<title>notes</title>
				<style>
					.tags {
						float: left;
						min-width: 100px;
					}
				</style>
			</head>
			<body>
				<table>
					<tr>
						<td class='tags'>
							<xsl:apply-templates select='tags/tag' />
						</td>
						<td>
							<xsl:apply-templates select='note' />
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
