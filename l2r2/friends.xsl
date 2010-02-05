<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:template match='entries'>
		<item>
			<guid isPermaLink='true'><xsl:value-of select='@journalurl'/>/<xsl:value-of select='@ditemid'/>.html</guid>
			<pubDate><xsl:value-of select='@logtime'/></pubDate>
			<title><xsl:value-of select='@subject_raw'/></title>
			<link><xsl:value-of select='@journalurl'/>/<xsl:value-of select='@ditemid'/>.html</link>
			<description><xsl:value-of select='@event_raw'/></description>
		</item>
	</xsl:template>
	<xsl:template match='/opt'>
		<rss version='2.0' xmlns:lj='http://www.livejournal.org/rss/lj/1.0/' xmlns:media='http://search.yahoo.com/mrss/' xmlns:atom10='http://www.w3.org/2005/Atom'>
			<channel>
				<title><xsl:value-of select='@login'/>'s friends</title>
				<link>http://livejournal.com/~<xsl:value-of select='@login'/>/friends</link>
				<description><xsl:value-of select='@login'/>'s friends</description>
				<xsl:apply-templates select='entries'/>
			</channel>
		</rss>
	</xsl:template>
</xsl:stylesheet>