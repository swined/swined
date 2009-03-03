<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='/nb/note/tag'>
		<span class='notetag'><xsl:value-of select='.'/></span>
	</xsl:template>
	<xsl:template match='/nb/note'>
		<form action='/nb/edit' method='post'>
			<xsl:element name='table'>
				<xsl:attribute name='id'>note_<xsl:value-of select='id'/></xsl:attribute>
				<xsl:attribute name='class'>note</xsl:attribute>
				<tr>
					<td class='notetext'>
						<xsl:element name='input'>
							<xsl:attribute name='type'>hidden</xsl:attribute>
							<xsl:attribute name='name'>id</xsl:attribute>
							<xsl:attribute name='value'><xsl:value-of select='id'/></xsl:attribute>
						</xsl:element>
						<textarea name='text' class='notearea' onChange='updheight(this)'><xsl:value-of select='text'/></textarea>
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
						<input type='submit' value='save'/>
					</td>
				</tr>
			</xsl:element>
		</form>
	</xsl:template>
	<xsl:template match='/nb'>
		<html>
			<head>
				<title>notes</title>
				<link rel='stylesheet' href='/static/notes.css' />
				<script>
					function updheight(textarea) {
						textarea.rows = textarea.value.split("\n").length;
					}
				</script>
			</head>
			<body>
				<a href='/nb/create'>create new</a>
				<xsl:apply-templates select='note' />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>