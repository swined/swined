#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;
use URI::Escape;
use MIME::Base64;
use File::Path;

my $wt = new WT;
my $c = $wt->conf;
my $dbh = $wt->dbh;

# syncdb
$wt->syncdb;

# delete overseeded
$dbh->do('UPDATE torrents SET del = 1 WHERE maxratio > 0 AND up/down > maxratio AND progress = 100');

# stop deleting
$dbh->do('UPDATE torrents SET active = 0 WHERE del > 0');

# kill
my @t = map { $_->[0] } @{$wt->selectall_arrayref('SELECT pid FROM torrents WHERE active = 0 AND pid > 0')};
kill(9, @t) and sleep 3 if @t;

# clean pid
my $t = $wt->selectall_arrayref('SELECT id,pid FROM torrents WHERE pid > 0');
$dbh->do('UPDATE torrents SET pid = 0 WHERE id = ?', undef, $_->[0]) for grep { ! -e "/proc/$_->[1]" } @$t;

# delete
my $sth = $dbh->prepare('SELECT * FROM torrents WHERE active = 0 AND pid = 0 AND del > 0');
$sth->execute;
while (my $r = $sth->fetchrow_hashref) {
	next if not $r->{owner} or $r->{owner} =~ /\W/;
	rmtree([$r->{output}], 1, 1) if $r->{output};
	next if -e $r->{output};
	$dbh->do('DELETE FROM torrents WHERE id = ?', undef, $r->{id});
}

# count vmsize and vmrss
$dbh->do(
	'UPDATE torrents SET vmsize = ?, vmrss = ? WHERE id = ?',
	undef,
	$_->{vmsize},
	$_->{vmrss},
	$_->{id},
) for map {
	my $r = { id => $_->[0] };
	open my $f, '<', '/proc/' . $_->[1] . '/status';
	my $s = join '', <$f>;
	close $f;
	$r->{vmsize} = [grep s/^VmSize:\s+(\d+)\s+.*/$1/, split /\n/, $s]->[0];
	$r->{vmrss} = [grep s/^VmRSS:\s+(\d+)\s+.*/$1/, split /\n/, $s]->[0];
	$r;
} @{$wt->selectall_arrayref('SELECT id,pid FROM torrents WHERE pid > 0')};

# spawn
my $sth = $dbh->prepare('SELECT * FROM torrents WHERE active > 0 AND pid = 0 AND del = 0');
$sth->execute;
while (my $r = $sth->fetchrow_hashref) {
	my $p = join ' ', map { WT::shesc($_) } $r->{id}, $c->{dbhost}, $c->{dbuser}, $c->{dbpass}, $c->{dbname}, '--minport', ($c->{minport} or '49000'), '--maxport', ($c->{maxport} or '49999');
	next if not $r->{owner} or $r->{owner} =~ /\W/;
	mkdir my $userdir = "/var/cache/webtornado/users/$r->{owner}";
	mkdir my $outdir = "$userdir/output";
	next unless -e $outdir;
	$dbh->do('UPDATE torrents SET outdir = ?, error = "", progress = 0, peers = 0, downrate = 0, uprate = 0, eta = 0 WHERE id = ?', undef, $outdir, $r->{id});
	print my $cmd = "/usr/share/webtornado/bin/download.py $p < /dev/null > /dev/null 2>&1 &";
	`$cmd`;
	exit;
}
