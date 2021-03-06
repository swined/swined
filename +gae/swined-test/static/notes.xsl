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
					<td>
						<xsl:element name='input'>
							<xsl:attribute name='type'>hidden</xsl:attribute>
							<xsl:attribute name='name'>id</xsl:attribute>
							<xsl:attribute name='value'><xsl:value-of select='id'/></xsl:attribute>
						</xsl:element>
						<xsl:element name='div'>
							<xsl:attribute name='class'>notetext</xsl:attribute>
							<xsl:attribute name='onClick'>make_editable(this)</xsl:attribute>
							<xsl:attribute name='value'><xsl:value-of select='text'/></xsl:attribute>
							<pre><xsl:value-of select='text'/></pre>
						</xsl:element>
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
					function make_editable(div) {
						var textarea = document.createElement('textarea');
						textarea.value = div.getAttribute('value');
						textarea.setAttribute('name', 'text');
						textarea.setAttribute('class', 'notearea');
						textarea.setAttribute('onKeyPress', 'updheight(this)');
						updheight(textarea);
						var parent = div.parentNode;
						parent.removeChild(div);
						parent.appendChild(textarea);
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