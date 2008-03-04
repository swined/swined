#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;
use URI::Escape;

$CGI::POST_MAX = 1 << 20;

my ($u, $c, $wt) = ($ENV{REMOTE_USER}, "/var/cache/webtornado/users/$ENV{REMOTE_USER}/output", new WT);

my $f = $wt->cgi->param('file') or die 'nothing uploaded';
my $tor = join '', <$f>; 
my ($bt, $tor) = (WT::getTorrentInfo($tor), uri_escape $tor);
my ($nf, $sz) = ("$c/$bt->{name}", $bt->{total_size} / (1 << 20));

my $r = $wt->dbh->selectrow_hashref('SELECT * FROM torrents WHERE owner = ? AND output = ?', undef, $u, $nf);
if ($r->{id}) {
    $wt->dbh->do('UPDATE torrents SET active = 0, progress = 0, maxratio = 0, size = ?, torrent = ? WHERE id = ?', undef, $sz, $tor, $r->{id});
} else {
    $wt->dbh->do('INSERT INTO torrents(owner, output, size, torrent) VALUES(?, ?, ?, ?)', undef, $u, $nf, $sz, $tor);
}
print $wt->cgi->header(-location => '/', -status => 302);