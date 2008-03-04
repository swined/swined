#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;
use URI::Escape;
use MIME::Base64;
use Data::Dumper;

my $wt = new WT;
my $c = $wt->conf;
my $dbh = $wt->dbh;

# syncdb
$wt->syncdb;

# suck .torrents to db
# my $sth = $dbh->prepare('SELECT * FROM torrents WHERE filename IS NOT NULL AND torrent IS NULL');
# $sth->execute;
# while (my $r = $sth->fetchrow_hashref) {
#     next unless -e $r->{filename};
#     $dbh->do('UPDATE torrents SET torrent = ? WHERE id = ?', 
# 	undef, uri_escape(WT::cat($r->{filename})), $r->{id});
# }

# delete overseeded
$dbh->do('UPDATE torrents SET del = 1 WHERE maxratio > 0 AND size > 0 AND up/down > maxratio AND progress = 100');

# stop deleting
$dbh->do('UPDATE torrents SET active = 0 WHERE del > 0');

# kill 
my @t = map { $_->[0] } @{$wt->selectall_arrayref('SELECT pid FROM torrents WHERE active = 0 AND pid > 0')};
kill(9, @t) and sleep 3 if @t;

# clean pid
my $t = $wt->selectall_arrayref('SELECT id,pid FROM torrents WHERE pid > 0');
$dbh->do('UPDATE torrents SET pid = 0 WHERE id = ?', undef, $_->[0]) for grep { ! -e "/proc/$_->[1]" } @$t;

# clean shit
# $dbh->do('UPDATE torrents SET error = "", downrate = "", uprate = "", eta = "" WHERE active = 0 AND pid = 0');

# delete
my $sth = $dbh->prepare('SELECT * FROM torrents WHERE active = 0 AND pid = 0 AND del > 0');
$sth->execute;
while (my $r = $sth->fetchrow_hashref) {
    next if not $r->{owner} or $r->{owner} =~ /\W/;
    if ($r->{output}) {
	my $out = WT::shesc($r->{output});
	`rm -rf $out`;
    }
    if ($r->{filename}) {
	my $tor = WT::shesc($r->{filename});
	`rm -rf $tor`;
    }
    next if -e $r->{output};
    next if -e $r->{filename};
    $dbh->do('DELETE FROM torrents WHERE id = ?', undef, $r->{id});
}

# spawn
my $sth = $dbh->prepare('SELECT * FROM torrents WHERE active > 0 AND pid = 0 AND del = 0');
$sth->execute;
while (my $r = $sth->fetchrow_hashref) {
    my $p = join ' ', map { WT::shesc($_) } $r->{id}, $c->{dbhost}, $c->{dbuser}, $c->{dbpass}, $c->{dbname}, '--minport', ($c->{minport} or '49000'), '--maxport', ($c->{maxport} or '49999');
    next if not $r->{owner} or $r->{owner} =~ /\W/;
    my $cachedir = "/var/cache/webtornado";
    my $userdir = "$cachedir/users/$r->{owner}";
    my $outdir = "$userdir/output";
    mkdir $userdir;
    mkdir $outdir;
    next unless -e $outdir;
    $dbh->do('UPDATE torrents SET outdir = ?, error = "", progress = 0, peers = 0, downrate = 0, uprate = 0, eta = 0 WHERE id = ?', undef, $outdir, $r->{id});
#     my $s = WT::shesc("begin-base64 644 file\n" . encode_base64(uri_unescape $r->{torrent}) . "====");
	my $s = WT::shesc(encode_base64(uri_unescape $r->{torrent}));
    `perl -MMIME::Base64 -e "print decode_base64 join '', <>" | /usr/share/webtornado/bin/download.py $p > /dev/null 2>&1 &`;
}
