#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;

my $wt = new WT;

my $tor = $wt->selectrow_hashref('SELECT * FROM torrents WHERE id = ?', undef, $wt->cgi->param('id'));
my $bt = WT::getTorrentInfo(uri_unescape $tor->{torrent});
my $ts = 0;
$ts += 512 + $_->{size} + ($_->{size} % 512) ? (512 - $_->{size} % 512) : 0 for @{$bt->{files}};

print($wt->cgi->header(-status => 404)) and exit unless $tor->{output} and -e $tor->{output};
print $wt->cgi->header(
	-content_type => 'application/x-tar',
	-content_length => $ts,
);

$tor->{output} =~ m{^(.*)/};
chdir $1;
open STDIN, 'tar c ' . WT::shesc($') . ' |'; #'
print while <>;
