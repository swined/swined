#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;
use CGI qw/:all/;
use CGI::Debug;
use XML::Writer;
use URI::Escape;
use Filesys::Statvfs;
use Time::Duration;

my $wt = new WT;

$wt->dbh->do(
	'UPDATE torrents SET active = 0 WHERE id = ? AND owner = ?',
	undef, param('id'), $ENV{REMOTE_USER},
) if param('a') eq 'stop';

$wt->dbh->do(
	'UPDATE torrents SET active = 1 WHERE id = ? AND owner = ?',
	undef, param('id'), $ENV{REMOTE_USER},
) if param('a') eq 'start';

my @torrents = sort { $b->{ratio} <=> $a->{ratio} } values %{$wt->dbh->selectall_hashref(
	'SELECT *,up/down AS ratio FROM torrents WHERE owner = ? AND del = 0',
	'id',
	undef,
	$ENV{REMOTE_USER},
)};

print CGI::header(-content_type => 'text/xml; charset=UTF-8');
my $xml = new XML::Writer(OUTPUT => *STDOUT, DATA_MODE => 1, DATA_INDENT => 4);
$xml->xmlDecl('UTF-8');
$xml->pi('xml-stylesheet', 'href="/webtornado/list.xsl" type="text/xsl"');
$xml->startTag('webtornado');
my @df = statvfs '/var/cache/webtornado/users';
$xml->emptyTag('disk', 'free' => $df[0]*$df[3], 'total' => $df[0]*$df[2]);
$xml->startTag('torrents');
foreach my $torrent (@torrents) {
	$torrent->{$_} ||= 0 for 'ratio', 'progress';
	$torrent->{$_} *= 1 << 20 for 'up', 'down';
	my $meta = WT::getTorrentInfo(uri_unescape $torrent->{torrent});
	$torrent->{eta} = ($torrent->{down} * $torrent->{maxratio} - $torrent->{up}) / $torrent->{uprate}
		if $torrent->{progress} == 100 and $torrent->{ratio} < $torrent->{maxratio} and $torrent->{uprate};
	my %attr = (
		'size' => $meta->{total_size},
		($torrent->{eta} ? ('eta' => duration($torrent->{eta}, 1)) : ()),
		(map { ($_ => $torrent->{$_}) } 'active', 'pid', 'maxratio', 'peers', 'progress', 'up', 'down', 'id'),
	);
	$xml->startTag('torrent', %attr);
	$xml->dataElement('name', $meta->{name});
	$xml->dataElement('announce', $1) if $meta->{announce} =~ m|^https?://([^/:]+)(?::\d+)?/.*$|i;
	$xml->dataElement('error', $torrent->{error}) if $torrent->{error};
	$xml->emptyTag('speed', 'up' => $torrent->{uprate}, 'down' => $torrent->{downrate});
	$xml->endTag('torrent');
}
$xml->endTag('torrents');
$xml->endTag('webtornado');
$xml->end;