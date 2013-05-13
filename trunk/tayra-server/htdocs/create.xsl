<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' doctype-system="http://www.w3.org/TR/html4/strict.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent='yes' />
	<xsl:template match='/root'>
		<div>
			<form action='add.pl'>
				<input type='hidden' name='location' value='{@location}'/>
				<div>
				  Major
				  <input type='text' name='major' />
				</div>
				<div>
				  Minor
				  <input type='text' name='minor' />
				</div>
				<div>
				  Type
				  <input type='text' name='type' />
				</div>
				<input type='submit'/>
			</form>
		</div>
	</xsl:template>
</xsl:stylesheet>
