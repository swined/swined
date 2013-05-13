<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' doctype-system="http://www.w3.org/TR/html4/strict.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent='yes' />
	<xsl:template match='/root'>
		<div>
			Upload:
			<form action='upload.pl' method='post' enctype='multipart/form-data'>
				<input type='file' name='file'/>
				<input type='submit'/>
			</form>
		</div>
		<xsl:apply-templates select='release'/>
	</xsl:template>
	<xsl:template match='/root/release'>
	    <div>
	      <a href='{@location}'>
		<xsl:value-of select='@id'/>
		/ 
		<xsl:value-of select='@major'/>.<xsl:value-of select='@minor'/>
		/
		<xsl:value-of select='@type'/>
	      </a>
	      [<a href='rm.pl?id={@id}'>X</a>]
	    </div>
	</xsl:template>
</xsl:stylesheet>
