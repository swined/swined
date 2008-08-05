<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:template match='/webtornado'>
		<html>
			<head>
				<meta http-equiv='REFRESH' content='30;URL=?'></meta>
				<style>
					BODY {
						font-family: verdana;
					}
					TABLE {
						width: 100%;
					}
					.head>TD {
						background-color: #CCCCCC;
						font-weight: bold;
					}
					TD {
						text-align: center;
					}
					TD.name {
						text-align: left;
					}
					.announce {
						color: #007700;
						font-size: 70%;
					}
					.error {
						color: #770000;
						font-size: 70%;
					}
					DIV.pb {
						border: 1px solid black;
						height: 5px;
						width: 100px;
						background-color: black;
						text-align: left;
					}
					DIV.pb>DIV {
						height: 100%;
						background-color: #00FF00;
					}
				</style>
			</head>
			<body>
				<xsl:apply-templates select='disk' />
				<br />
				<xsl:apply-templates select='torrents' />
			</body>
		</html>
	</xsl:template>
	<xsl:template match='torrents'>
		<table>
			<tr class='head'>
				<td>name</td>
				<td>size</td>
				<td>up</td>
				<td>down</td>
				<td>peers</td>
				<td>ratio</td>
				<td>speed</td>
				<td>status</td>
			</tr>
			<xsl:apply-templates select='torrent' />
			<tr class='head'>
				<td>total</td>
				<td>
					<xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrent/@size)'/>
					</xsl:call-template>
				</td>
				<td>
					<xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrent/@up)'/>
					</xsl:call-template>
				</td>
				<td>
					<xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrent/@down)'/>
					</xsl:call-template>
				</td>
				<td>
					<xsl:value-of select='sum(torrent[@active > 0 and @pid > 0]/@peers)' />
				</td>
				<td>ratio</td>
				<td>speed</td>
				<td>status</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match='torrent'>
		<xsl:variable name='ratio'>
			<xsl:choose>
				<xsl:when test='@up and @down > 0'>
					<xsl:value-of select='@up div @down' />
				</xsl:when>
				<xsl:otherwise>
					0
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name='running'>
			<xsl:value-of select='@active * @pid > 0' />
		</xsl:variable>
		<xsl:element name='tr'>
			<xsl:if test='position() mod 2 = 0'>
				<xsl:attribute name='style'>background-color: #eeeeee</xsl:attribute>
			</xsl:if>
			<td class='name'>
				<xsl:value-of select='name' />
				<div class='announce'><xsl:value-of select='announce' /></div>
				<xsl:if test='error'><div class='error'><xsl:value-of select='error' /></div></xsl:if>
			</td>
			<td>
				<xsl:call-template name='sz'>
					<xsl:with-param name='val' select='@size'/>
				</xsl:call-template>
			</td>
			<td>
				<xsl:choose>
					<xsl:when test='@maxratio > 0 and @maxratio >= $ratio'>
						-<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='@up * (1 - $ratio div @maxratio)'/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test='@maxratio > 0 and @maxratio &lt; $ratio'>
						+<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='@up * ($ratio div @maxratio - 1)'/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='@up'/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td>
				<xsl:call-template name='sz'>
					<xsl:with-param name='val' select='@down'/>
				</xsl:call-template>
			</td>
			<td>
				<xsl:if test='$running'>
					<xsl:if test='@peers > 0'>
						<xsl:value-of select='@peers' />
					</xsl:if>
					<xsl:if test='@peers = 0'>none</xsl:if>
				</xsl:if>
				<xsl:if test='not($running)'>--</xsl:if>
			</td>
			<td>
				<xsl:value-of select='format-number($ratio, "0.#")' />
				<xsl:if test='@maxratio > 0'>
					<xsl:text> </xsl:text>
					(<xsl:value-of select='@maxratio' />)
				</xsl:if>
			</td>
			<xsl:apply-templates select='speed' />
			<td>
				<xsl:if test='@progress = 100 and $running'><div>seeding</div></xsl:if>
				<xsl:if test='@progress = 100 and not($running)'><div>done</div></xsl:if>
				<xsl:if test='@progress = 0 and not($running)'><div>unknown</div></xsl:if>
				<xsl:if test='@eta and $running'><div>eta <xsl:value-of select='@eta' /></div></xsl:if>
				<xsl:if test='@progress > 0 and @progress &lt; 100'>
					<center><div style='width: 100px' class='pb'><xsl:element name='div'>
					<xsl:attribute name='style'>width: <xsl:value-of select='@progress'/>%</xsl:attribute>
					</xsl:element></div></center>
				</xsl:if>
			</td>
		</xsl:element>
	</xsl:template>
	<xsl:template match='disk'>
		<center>
			diskspace:
			<xsl:call-template name='sz'>
				<xsl:with-param name='val' select='@free'/>
			</xsl:call-template>
			of
			<xsl:call-template name='sz'>
				<xsl:with-param name='val' select='@total'/>
			</xsl:call-template>
			free
			<div style='width: 97%' class='pb'>
				<xsl:element name='div'>
					<xsl:attribute name='style'>
						width:
						<xsl:value-of select='100 * (1 - @free div @total)' />%
					</xsl:attribute>
				</xsl:element>
			</div>
		</center>
	</xsl:template>
	<xsl:template match='speed'>
		<td>
			<xsl:choose>
				<xsl:when test='not(../@active > 0 and ../@pid > 0)'>--</xsl:when>
				<xsl:when test='@up > 0 or @down > 0'>
					<xsl:if test='@down != 0'>
						<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='@down'/>
						</xsl:call-template>
						/
					</xsl:if>
					<xsl:call-template name='sz'>
						<xsl:with-param name='val' select='@up'/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>stalled</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<xsl:template name='sz'>
		<xsl:param name='val' />
		<xsl:choose>
			<xsl:when test='$val > 1024 * 1024 * 1024 * 1024'>
				<xsl:value-of select='format-number($val div (1024 * 1024 * 1024 * 1024), "0.#")' />T
			</xsl:when>
			<xsl:when test='$val > 1024 * 1024 * 1024'>
				<xsl:value-of select='format-number($val div (1024 * 1024 * 1024), "0.#")' />G
			</xsl:when>
			<xsl:when test='$val > 1024 * 1024'>
				<xsl:value-of select='format-number($val div (1024 * 1024), "0.#")' />M
			</xsl:when>
			<xsl:when test='$val > 1024'>
				<xsl:value-of select='format-number($val div 1024, "0.#")' />k
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select='format-number($val, "0.#")' />b
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>