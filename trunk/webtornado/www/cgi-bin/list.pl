#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use CGI qw/:all/;
use CGI::Debug;
use WT;
use VER;
use Time::HiRes qw/time/;
use HTML::Template;
use Filesys::Statvfs;
use URI::Escape;
use Time::Duration;
use Cache::FastMmap;

my $tm = time;
my $wt = new WT;

my $metacache = new Cache::FastMmap(
	share_file => '/var/cache/webtornado/metacache',
	expire_time => '1d',
	unlink_on_exit => 0,
	read_cb => sub { WT::getTorrentInfo(uri_unescape $wt->dbh->selectrow_hashref('SELECT torrent FROM torrents WHERE owner = ? AND sha1(torrent) = ?', undef, $ENV{REMOTE_USER}, $_[1])->{torrent}) },
);

sub r10 { int(10 * (shift or $_)) / 10 }
sub fmsz { local $i = 3, (map { return r10($n) . $_ if 0.99 < abs(local $n = $_[0] / (1 << 10 * $i--)) } split '', 'GMkb'), return 0 }

sub progressbar {
	my ($p, $e, $w) = @_;
	return 'unknown' unless $p;
	return 'done' if $p >= 100;
	$p .= '%' unless $p =~ /%$/;
	center(($e ? 'eta ' . duration($e, 1) : '') . div({ -style => 'width: ' . ($w or '100px'), -class => 'pbo' }, div({ -style => 'width: ' . int($p) . '%', -class => 'pbi' })));
}

my ($t, $q, @torrents) = ({}, $wt->dbh->selectall_hashref('SELECT *,up/down AS ratio,sha1(torrent) AS metahash,"" AS torrent FROM torrents WHERE owner = ?', 'id', undef, $ENV{REMOTE_USER}));
foreach my $r (sort { $b->{ratio} <=> $a->{ratio} } map { $q->{$_} } keys %$q) {
	$r->{$_} *= 1 << 20 for 'up', 'down';
	my $bt = eval { alarm 0; $metacache->get($r->{metahash}) };
	$r->{size} = $bt->{total_size};
	$r->{done} = $r->{progress} >= 100;
	$t->{count}++;
	$t->{active}++ if $r->{active};
	$t->{$_} += $r->{$_} for 'size', 'up', 'down';
	$t->{progress} += int($r->{progress} * $r->{size} / 100);
	$r->{active} and $t->{$_} += $r->{$_} for 'downrate', 'uprate', 'peers';
	$t->{has_undone} = 1 unless $r->{done};
	(my $up = $r->{maxratio} ? '-' . fmsz($r->{down} * $r->{maxratio} - $r->{up}) : fmsz($r->{up})) =~ s/^--(.)/+$1/g;
	my $fc = scalar @{$bt->{files}};
	$r->{seedstatus} = progressbar(100 * $r->{ratio} / $r->{maxratio}, $r->{uprate} ? ($r->{down} * $r->{maxratio} - $r->{up}) / $r->{uprate} : 0) if $r->{progress} == 100 and $r->{ratio} < $r->{maxratio};
	$r->{$_} = fmsz($r->{$_}) for 'size', 'down', 'uprate', 'downrate';
	$r->{$_} = r10($r->{$_}) for 'ratio', 'maxratio';
	push @torrents, {
		%$r,
		user => $ENV{REMOTE_USER},
		name => $bt->{name},
		ue_name => uri_escape($bt->{name}),
		overseed => ($r->{maxratio} and ($r->{ratio} > $r->{maxratio})),
		files => ($fc > 1 ? [ map {{ size => fmsz($_->{size}), name => $_->{name}, user => $ENV{REMOTE_USER} }} @{$bt->{files}} ] : []),
		files_count => $fc,
		up => $up,
		status => progressbar($r->{progress}, $r->{eta}),
	};
}

my @pb = statvfs '/var/cache/webtornado/users';
my $tmpl = new HTML::Template(filename => '/usr/share/webtornado/tmpl/list.tmpl', die_on_bad_params => 0, vanguard_compatibility_mode => 1, loop_context_vars => 1);
$tmpl->param({
	%$t,
	(map { ("total_$_" => $t->{$_}) } keys %$t),
	(map { ("total_$_" => fmsz($t->{$_})) } 'size', 'up', 'down', 'uprate', 'downrate'),
	disk_free => fmsz($pb[0]*$pb[3]),
	disk_total => fmsz($pb[0]*$pb[2]),
	disk_progressbar => progressbar(int(100*(1-$pb[3]/$pb[2])), 0, '97%'),
	torrents => [@torrents],
	total_ratio => ($t->{up} and $t->{down}) ? r10($t->{up} / $t->{down}) : '--',
	total_status => progressbar($t->{has_undone} ? int(100 * $t->{progress} / ($t->{size} or 1)) : 100),
	version => $VER::VER,
	gtime => int((time()-$tm)*1000)/1000,
});
print header(-content_type => 'text/html; charset=utf-8') . $tmpl->output;