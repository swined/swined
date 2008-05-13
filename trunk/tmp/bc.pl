#!/usr/bin/perl

use DBI;
use POSIX;
use Chart::Plot;

my $tms = 'PEM 2.6 update 2';
my $dbh = DBI->connect('dbi:mysql:dbname=bugzilla', 'root', ',fypfq');

my $fields = $dbh->selectall_hashref('SELECT * FROM fielddefs', 'fieldid');
my $bugs = $dbh->selectall_hashref('SELECT * FROM (SELECT bug_id AS _id FROM bugs 
	WHERE target_milestone = ? UNION SELECT bug_id AS _id FROM bugs_activity 
	WHERE fieldid = 26 AND (added = ? OR removed = ?)) AS x INNER JOIN bugs 
	ON _id = bug_id', 'bug_id', undef, $tms, $tms, $tms) or die "zarro boogs found\n";
my $sth = $dbh->prepare('SELECT * FROM (SELECT bug_id AS _id FROM bugs WHERE 
	target_milestone = ? UNION SELECT bug_id AS _id FROM bugs_activity WHERE 
	fieldid = 26 AND (added = ? OR removed = ?)) AS x INNER JOIN bugs_activity 
	ON _id = bug_id WHERE fieldid IN (8, 26) ORDER BY bug_when DESC');
$sth->execute($tms, $tms, $tms);
my ($dump, $cdate) = {};
while (my $r = $sth->fetchrow_hashref) {
	my $rdate = [split ' ', $r->{bug_when}]->[0];
	map { $dump->{$cdate}->{$_->{bug_status}}++ } grep { $tms eq 
		$_->{target_milestone} } values %$bugs if $cdate and $rdate ne $cdate;
	$cdate = $rdate;
	$bugs->{$r->{bug_id}}->{$fields->{$r->{fieldid}}->{name}} = $r->{removed};
	$bugs->{$_} = undef for grep { $bugs->{$_}->{creation_ts} ge $cdate } keys %$bugs;
}

my @skd = sort keys %$dump;

for (my $i = 0; $i < scalar(@skd) - 1; $i++) {
	next unless $skd[$i] =~ /^(\d{4})-(\d{2})-(\d{2})$/;
	my $n = strftime('%Y-%m-%d', 0, 0, 0, $3 + 1, $2 - 1, $1 - 1900);
	$dump->{$n} = $dump->{$skd[$i]} unless $dump->{$n};
}

sub ticks {
	my ($r, $c) = ({}, shift() - 1);
	$r->{ int($_ / $c * 100) } = $_[ int($_ / $c * (scalar(@_) - 1)) ] for 0 .. $c;
	return $r;
}

my $img = new Chart::Plot(800, 600);
my $xv = [ map { $_ * 100 / (scalar(@skd) - 1) } 0 .. scalar(@skd) - 1 ];
$img->setData($xv, [ map { $dump->{$_}->{NEW} or 0 } @skd ], 'redNOPOINTS');
$img->setData($xv, [ map { $dump->{$_}->{VERIFIED} or 0 } @skd ], 'greenNOPOINTS');
$img->setData($xv, [ map { $dump->{$_}->{RESOLVED} or 0 } @skd ], 'blueNOPOINTS');
$img->setGraphOptions (title => $tms, xTickLabels => ticks(10, @skd));
open (STDOUT, '> plot.png') or die "Failed to write file: $!\n";
print $img->draw;
