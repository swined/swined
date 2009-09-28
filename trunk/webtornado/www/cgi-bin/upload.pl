#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;
use URI::Escape;
use MIME::Base64;

$CGI::POST_MAX = 1 << 20;

my ($u, $c, $wt) = ($ENV{REMOTE_USER}, "/var/cache/webtornado/users/$ENV{REMOTE_USER}/output", new WT);

my $f = $wt->cgi->param('file') or die 'nothing uploaded';
my $tor = join '', <$f>;
my ($bt, $tor) = (WT::getTorrentInfo($tor), encode_base64 $tor);
my $nf = "$c/$bt->{name}";

my $r = $wt->dbh->selectrow_hashref('SELECT * FROM torrents WHERE owner = ? AND output = ?', undef, $u, $nf);
if ($r->{id}) {
    $wt->dbh->do('UPDATE torrents SET active = 0, progress = 0, maxratio = 0, torrent = ? WHERE id = ?', undef, $tor, $r->{id});
} else {
    $wt->dbh->do('INSERT INTO torrents(owner, output, torrent, pid, active, del, up, down, progress, maxratio, peers, peerlist, show_peers, vmsize, vmrss) VALUES(?, ?, ?, 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0)', undef, $u, $nf, $tor);
}
print $wt->cgi->header(-location => $ENV{HTTP_REFERER}, -status => 302);
