<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='/nb/tags/tag'>
		<xsl:value-of select='.'/><br/>
	</xsl:template>
	<xsl:template match='/nb/note/tag'>
		<span class='notetag'><xsl:value-of select='.'/></span>
	</xsl:template>
	<xsl:template match='/nb/note'>
		<xsl:element name='table'>
			<xsl:attribute name='id'>note_<xsl:value-of select='id'/></xsl:attribute>
			<tr>
				<td class='notetext'>
					<xsl:value-of select='text'/>
				</td>
			</tr>
			<tr>
				<td class='notefoot'>
					<xsl:apply-templates select='tag'/>
					<span class='notemtime'><xsl:value-of select='mtime'/></span>
					<xsl:element name='a'>
						<xsl:attribute name='href'>javascript:delete_note('<xsl:value-of select='id'/>')</xsl:attribute>
						del
					</xsl:element>
				</td>
			</tr>
		</xsl:element>
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
					.notetext {
						font-size: 70%;
					}
					.notefoot {
						font-size: 70%;
						background-color: #eeeeee;
					}
					.notetag {
						color: green;
						margin-right: 5px;
					}
					.notemtime {
						color: gray;
					}
				</style>
				<script src='/static/ajax.js' />
				<script>
					function delete_note(id) {
						var note = document.getElementById('note_' + id);
						note.parentNode.removeChild(note);
						xhr('/nb/delete/' + id);
					}
				</script>
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
