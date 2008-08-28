<?xml version='1.0' encoding='UTF-8'?>

<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes' />
	<xsl:variable name='nf'>0.##</xsl:variable>
	<xsl:template match='/webtornado'>
		<html>
			<head>
				<meta http-equiv='REFRESH' content='30;URL=?'></meta>
				<title>
					[wt]
					<xsl:if test='sum(torrents/torrent[@active * @pid > 0]/speed/@down) > 0'>
						d:<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='sum(torrents/torrent[@active * @pid > 0]/speed/@down)'/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test='sum(torrents/torrent[@active * @pid > 0]/speed/@up) > 0'>
						u:<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='sum(torrents/torrent[@active * @pid > 0]/speed/@up)'/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test='sum(torrents/torrent[@active * @pid > 0]/@peers) > 0'>
						p:<xsl:value-of select='sum(torrents/torrent[@active * @pid > 0]/@peers)'/>
					</xsl:if>
					<xsl:if test='sum(torrents/torrent/@down) > 0'>
						r:<xsl:value-of select='format-number(sum(torrents/torrent/@up) div sum(torrents/torrent/@down), $nf)' />
					</xsl:if>
				</title>
				<style>
					BODY {
						font-family: verdana;
					}
					TABLE {
						width: 100%;
					}
					IMG {
						border: 0;
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
				<center>
					vmsize: <xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrents/torrent[@pid > 0]/@vmsize) * 1024'/>
					</xsl:call-template>,
					vmrss: <xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrents/torrent[@pid > 0]/@vmrss) * 1024'/>
					</xsl:call-template>
				</center>
				<xsl:apply-templates select='disk' />
				<br />
				<xsl:apply-templates select='torrents' />
				<center>
					<font color='gray'>webtornado <xsl:value-of select='@version' /> © swined</font>
				</center>
			</body>
		</html>
	</xsl:template>
	<xsl:template match='torrents'>
		<table>
			<tr class='head'>
				<td></td>
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
				<td>
					<nbsp>
						<xsl:value-of select='count(torrent[@active > 0])' />
						/
						<xsl:value-of select='count(torrent)' />
					</nbsp>
				</td>
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
				<td>
					<xsl:value-of select='format-number(sum(torrent/@up) div sum(torrent/@down), $nf)' />
				</td>
				<td>
					<xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrents/torrent[@active * @pid > 0]/speed/@down)'/>
					</xsl:call-template>
					<xsl:call-template name='sz'>
						<xsl:with-param name='val' select='sum(torrents/torrent[@active * @pid > 0]/speed/@up)'/>
					</xsl:call-template>
				</td>
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
			<xsl:value-of select='@active * @pid' />
		</xsl:variable>
		<xsl:variable name='progress'>
			<xsl:choose>
				<xsl:when test='@progress = 100 and @maxratio > 0'>
					<xsl:value-of select='100 * ($ratio div @maxratio)'/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select='@progress' />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name='tr'>
			<xsl:if test='position() mod 2 = 0'>
				<xsl:attribute name='style'>background-color: #eeeeee</xsl:attribute>
			</xsl:if>
			<td>
				<xsl:choose>
					<xsl:when test='@active * @pid > 0'>
						<xsl:element name='a'>
							<xsl:attribute name='href'>?a=stop&amp;id=<xsl:value-of select='@id' /></xsl:attribute>
							<img src='/webtornado/img/green.gif' />
						</xsl:element>
					</xsl:when>
					<xsl:when test='(@active + @pid) > 0'>
						<img src='/webtornado/img/yellow.gif' />
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name='a'>
							<xsl:attribute name='href'>?a=start&amp;id=<xsl:value-of select='@id' /></xsl:attribute>
							<img src='/webtornado/img/black.gif' />
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test='@progress = 100'>
					<xsl:element name='a'>
						<xsl:attribute name='href'>/webtornado<xsl:choose><xsl:when test='@files = 1'>-users/<xsl:value-of select='/webtornado/@user' />/output/<xsl:value-of select='name' /></xsl:when><xsl:otherwise>/<xsl:value-of select='@id' />.tar</xsl:otherwise></xsl:choose></xsl:attribute>
						<img src='/webtornado/img/tar_down.gif' />
					</xsl:element>
				</xsl:if>
				<xsl:element name='a'>
					<xsl:attribute name='href'>?a=delete&amp;id=<xsl:value-of select='@id' /></xsl:attribute>
					<img src='/webtornado/img/delete.png' />
				</xsl:element>
			</td>
			<td class='name'>
				<xsl:value-of select='name' />
				<xsl:if test='@files > 1'>
					<span style='color: gray; margin-left: 3px'>
						[<xsl:value-of select='@files' /> files]
					</span>
				</xsl:if>
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
							<xsl:with-param name='val' select='@maxratio * @down - @up'/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test='@maxratio > 0 and @maxratio &lt; $ratio'>
						+<xsl:call-template name='sz'>
							<xsl:with-param name='val' select='@up - @maxratio * @down'/>
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
				<xsl:choose>
					<xsl:when test='$running = 0'>
						--
					</xsl:when>
					<xsl:when test='@peers > 0'>
						<xsl:value-of select='@peers' />
					</xsl:when>
					<xsl:when test='@peers = 0'>
						none
					</xsl:when>
					<xsl:otherwise>
						oops
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td><nobr>
				<xsl:value-of select='format-number($ratio, $nf)' />
				<xsl:if test='@maxratio > 0'>
					<xsl:text> </xsl:text>
					(<xsl:value-of select='@maxratio' />)
				</xsl:if>
			</nobr></td>
			<xsl:apply-templates select='speed' />
			<td>
				<xsl:if test='@progress = 100'><div>seeding</div></xsl:if>
				<xsl:if test='@progress = 0'><div>unknown</div></xsl:if>
				<xsl:if test='@eta and $running > 0'><div>eta <xsl:value-of select='@eta' /></div></xsl:if>
				<xsl:if test='$progress > 0 and $progress &lt; 100'>
					<center><div style='width: 100px' class='pb'><xsl:element name='div'>
					<xsl:attribute name='style'>width: <xsl:value-of select='$progress'/>%</xsl:attribute>
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
				<xsl:value-of select='format-number($val div (1024 * 1024 * 1024 * 1024), $nf)' />T
			</xsl:when>
			<xsl:when test='$val > 1024 * 1024 * 1024'>
				<xsl:value-of select='format-number($val div (1024 * 1024 * 1024), $nf)' />G
			</xsl:when>
			<xsl:when test='$val > 1024 * 1024'>
				<xsl:value-of select='format-number($val div (1024 * 1024), $nf)' />M
			</xsl:when>
			<xsl:when test='$val > 1024'>
				<xsl:value-of select='format-number($val div 1024, $nf)' />k
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select='format-number($val, $nf)' />b
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>