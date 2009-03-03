<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='/nb/note/tag'>
		<span class='notetag'><xsl:value-of select='.'/></span>
	</xsl:template>
	<xsl:template match='/nb/note'>
		<xsl:element name='table'>
			<xsl:attribute name='id'>note_<xsl:value-of select='id'/></xsl:attribute>
			<xsl:attribute name='class'>note</xsl:attribute>
			<tr>
				<td class='notetext'>
					<form action='/nb/edit' method='post'>
						<xsl:element name='input'>
							<xsl:attribute name='type'>hidden</xsl:attribute>
							<xsl:attribute name='name'>id</xsl:attribute>
							<xsl:attribute name='value'><xsl:value-of select='id'/></xsl:attribute>
						</xsl:element>
						<textarea name='text' class='notearea'><xsl:value-of select='text'/></textarea>
						<input type='submit' value='save'/>
					</form>
				</td>
			</tr>
			<tr>
				<td class='notefoot'>
					<xsl:apply-templates select='tag'/>
					<span class='notemtime'><xsl:value-of select='mtime'/></span>
					<xsl:element name='a'>
						<xsl:attribute name='href'>/nb/delete?id=<xsl:value-of select='id'/></xsl:attribute>
						<img src='/static/trash.png' />
					</xsl:element>
				</td>
			</tr>
		</xsl:element>
	</xsl:template>
	<xsl:template match='/nb'>
		<html>
			<head>
				<title>notes</title>
				<link rel='stylesheet' href='/static/notes.css' />
			</head>
			<body>
				<xsl:apply-templates select='note' />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>