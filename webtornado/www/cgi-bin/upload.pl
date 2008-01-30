#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;
use Data::Dumper;

$CGI::POST_MAX = 1024 * 1024;

my $wt = new WT;

my $c = "/var/cache/webtornado/users/$ENV{REMOTE_USER}";

mkdir($c) and mkdir("$c/torrents");
my $f = $wt->cgi->param('file') or die 'nothing uploaded';
my $tor; $tor .= $_ while <$f>;
open F, "> $c/torrents/$f";
print F $tor;
close F;

my $bt = WT::getTorrentInfo(WT::cat("$c/torrents/$f"));
(my $nf = $bt->{files}->[0]->{name}) =~ s{/.*}{};
my $r = $wt->dbh->selectrow_hashref(
    'SELECT * FROM torrents WHERE owner = ? AND output = ?',
    undef, $ENV{REMOTE_USER}, "$c/output/$nf");
if ($r->{id}) {
    $wt->dbh->do('UPDATE torrents SET active = 0, progress = 0, maxratio = 0, filename = ?, size = ?, info = ?, torrent = ? WHERE id = ?', 
	undef, "$c/torrents/$f", $bt->{total_size} / 1024 / 1024, Dumper($bt), $tor, $r->{id});
} else {
    $wt->dbh->do('INSERT INTO torrents(owner, filename, output, size, torrent, info) VALUES(?, ?, ?, ?, ?, ?)', 
	undef, $ENV{REMOTE_USER}, "$c/torrents/$f", "$c/output/$nf", $bt->{total_size} / 1024 / 1024, $tor, Dumper $bt);
}
print $wt->cgi->header(-location => '/', -status => 302);