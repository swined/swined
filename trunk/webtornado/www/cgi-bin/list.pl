#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use CGI qw/:all/;
use CGI::Debug;
use WT;
use VER;
use HTML::Template;
use Filesys::Statvfs;
use URI::Escape;
use Time::Duration;

my $wt = new WT;

sub A { a({ -href => shift }, join ' ', @_) }
sub IMG { img({ -src => shift }) }

sub r10 { int(10 * (shift or $_)) / 10 }

sub fmsz {
    my $s = shift;
    return r10 . 'T' if 0.7 < abs(local $_ = $s/1024/1024/1024/1024);
    return r10 . 'G' if 0.7 < abs(local $_ = $s/1024/1024/1024);
    return r10 . 'M' if 0.7 < abs(local $_ = $s/1024/1024);
    return r10 . 'k' if 0.7 < abs(local $_ = $s/1024);
    return int $s;
}

sub progressbar {
    my ($p, $e, $w) = @_;
    return 'unknown' unless $p;
    return 'done' if $p >= 100;
    $p .= '%' unless $p =~ /%$/;
    center(($e ? 'eta ' . duration($e, 1) : '') . div({ -style => 'width: ' . ($w or '100px'), -class => 'pbo' }, div({ -style => 'width: ' . int($p), -class => 'pbi' })));
}

my @torrents;
my $total = {};
my $sth = $wt->dbh->prepare('SELECT * FROM torrents WHERE owner = ? ORDER BY up/down DESC');
$sth->execute($ENV{REMOTE_USER});
while (my $r = $sth->fetchrow_hashref) {
	$r->{$_} *= 1 << 20 for 'up', 'down';
	my $bt = WT::getTorrentInfo(uri_unescape $r->{torrent});
	$r->{size} = $bt->{total_size};
	$r->{ratio} = $r->{down} ? $r->{up} / $r->{down} : 0;
	$r->{done} = $r->{progress} >= 100;
	$total->{count}++;
	$total->{active}++ if $r->{active};
	$total->{$_} += $r->{$_} for 'size', 'up', 'down';
	$total->{progress} += int($r->{progress} * $r->{size} / 100);
	$r->{active} and $total->{$_} += $r->{$_} for 'downrate', 'uprate', 'peers';
	$total->{has_undone} = 1 unless $r->{done};
	my $statusimg = A("/start/$r->{id}", IMG('/img/black.gif'));
	$statusimg = A("/stop/$r->{id}", IMG('/img/green.gif')) if $r->{active} and $r->{pid};
	$statusimg = IMG('/img/yellow.gif') if $r->{active} and ! $r->{pid} or ! $r->{active} and $r->{pid};
	(my $up = $r->{maxratio} ? '-' . fmsz($r->{down} * $r->{maxratio} - $r->{up}) : fmsz($r->{up})) =~ s/^--(.)/+$1/g;
	my $fc = scalar @{$bt->{files}};
	$r->{seedstatus} = progressbar(100 * $r->{ratio} / $r->{maxratio}, $r->{uprate} ? ($r->{down} * $r->{maxratio} - $r->{up}) / $r->{uprate} : 0) if $r->{progress} == 100 and $r->{ratio} < $r->{maxratio};
	push @torrents, {
		%$r,
		user => $ENV{REMOTE_USER},
		icons => $statusimg,
		name => $bt->{name},
		ue_name => uri_escape($bt->{name}),
		maxratio => r10($r->{maxratio}),
		overseed => ($r->{maxratio} and ($r->{ratio} > $r->{maxratio})),
		files => ($fc > 1 ? [ map {{
			size => fmsz($_->{size}),
			name => $_->{name},
			user => $ENV{REMOTE_USER}
		}} @{$bt->{files}} ] : []),
		files_count => $fc,
		size => fmsz($r->{size}),
		up => $up,
		down => fmsz($r->{down}),
		ratio => r10($r->{ratio}),
		uprate => fmsz($r->{uprate}),
		downrate => fmsz($r->{downrate}),
		status => progressbar($r->{progress}, $r->{eta}),
	};
}
$total->{active} ||= 0;

my @pb = statvfs '/var/cache/webtornado/users';

my $tmpl = new HTML::Template(
    filename => '/usr/share/webtornado/tmpl/list.tmpl',
    die_on_bad_params => 0,
    vanguard_compatibility_mode => 1,
    loop_context_vars => 1,
);
$tmpl->param({
	disk_free => fmsz($pb[0]*$pb[3]),
	disk_total => fmsz($pb[0]*$pb[2]),
	disk_progressbar => progressbar(int(100*(1-$pb[3]/$pb[2])), 0, '97%'),
	has_undone => $total->{has_undone},
	torrents => [@torrents],
	total_active => $total->{active},
	total_count => $total->{count},
	total_size => fmsz($total->{size}),
	total_up => fmsz($total->{up}),
	total_down => fmsz($total->{down}),
	total_peers => $total->{peers},
	total_ratio => ($total->{up} and $total->{down}) ? r10($total->{up} / $total->{down}) : '--',
	total_speed => (fmsz($total->{downrate}) or '0b') . ' / ' . (fmsz($total->{uprate}) or '0b'),
	total_status => progressbar($total->{has_undone} ? int(100 * $total->{progress} / ($total->{size} or 1)) : 100),
	version => $VER::VER,
});

print header(-content_type => 'text/html; charset=utf-8') . $tmpl->output;
