#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use CGI qw/:all/;
use CGI::Debug;
use WT;
use VER;
use HTML::Template;
use Filesys::DiskSpace;
use URI::Escape;
use Time::Duration;

my $wt = new WT;

print $wt->cgi->header(-content_type => 'text/html; charset=utf-8');

sub A { a({ -href => shift }, join ' ', @_) }
sub IMG { img({ -border => 0, -src => shift }) }

sub r10 { int(10 * (shift or $_)) / 10 }

sub fmsz {
    my $s = shift or return;
    return r10 . 'T' if 0.7 < abs(local $_ = $s/1024/1024/1024/1024);
    return r10 . 'G' if 0.7 < abs(local $_ = $s/1024/1024/1024);
    return r10 . 'M' if 0.7 < abs(local $_ = $s/1024/1024);
    return r10 . 'k' if 0.7 < abs(local $_ = $s/1024);
    return int($s) . 'b';
}

sub progressbar {
    my ($p, $e, $w) = @_;
    return 'unknown' unless $p;    
    return 'done' if $p >= 100;
    $p .= '%' unless $p =~ /%$/;
    center(($e ? 'eta ' . duration($e, 1) : '') . div({ -style => 'border: 1px solid black; width: ' . ($w or '100px') . 
	'; height: 5px; background-color: black; text-align: left'}, div({ -style => "width: ${p}; height: 100%; background-color: #00FF00" })));
}

my @torrents;
my $total = {};
my $sth = $wt->dbh->prepare('SELECT * FROM torrents WHERE owner = ? ORDER BY up/size DESC');
$sth->execute($ENV{REMOTE_USER});
while (my $r = $sth->fetchrow_hashref) {
    my $bt = WT::getTorrentInfo(uri_unescape $r->{torrent});
    $r->{size} = $bt->{total_size} / (1 << 20);
    $r->{ratio} = 100 * $r->{up} / ($r->{progress} or 0.001) / $r->{size};
    $r->{down} = $r->{progress} * $r->{size} / 100;
    $r->{done} = $r->{progress} >= 100;
    $total->{count}++;
    $total->{active}++ if $r->{active};
    $total->{size} += $r->{size};
    $total->{up} += $r->{up};
    $total->{progress} += int($r->{progress} * $r->{size} / 100);
    $total->{down} += $r->{down};
    $total->{downrate} += $r->{downrate};
    $total->{uprate} += $r->{uprate};
    $total->{has_undone} = 1 unless $r->{done};
    my $statusimg = A("/start/$r->{id}", IMG('/img/black.gif'));
    $statusimg = A("/stop/$r->{id}", IMG('/img/green.gif')) if $r->{active} and $r->{pid};
    $statusimg = IMG('/img/yellow.gif') if $r->{active} and ! $r->{pid} or ! $r->{active} and $r->{pid};
    $statusimg .= A("/$r->{id}.tar", IMG('/img/tar_down.gif')) if $r->{done} and not $r->{del};
    $statusimg .= A("/delete/$r->{id}", IMG('/img/delete.png')) unless $r->{del};
    my $files = join '<br>', map { '[' . fmsz($_->{size}) . '] ' . $_->{name} } @{$bt->{files}};
    my $fc = scalar @{$bt->{files}};
    $files = $fc > 1 ? "<div id='files_$r->{id}' style='color: #666666; display: inline'><a onclick='files_$r->{id}.innerHTML=files_$r->{id}_content.innerHTML'>[$fc files]</a></div><div style='display: none' id='files_$r->{id}_content'><br>$files</div>" : '';
    my $ratio = (r10($r->{ratio}) or '--') . ($r->{maxratio} ? ' (' . r10($r->{maxratio}) . ')' : '');
    $ratio = "<div style='color: red'><b>$ratio</b></div>" if $r->{maxratio} and ($r->{ratio} > $r->{maxratio});
    $ratio = "<a onclick='set_maxratio_$r->{id}.innerHTML=set_maxratio_$r->{id}_content.innerHTML'>$ratio</a>" .
    div({ -id => "set_maxratio_$r->{id}" }) .
    div({ -id => "set_maxratio_$r->{id}_content", -style => 'display: none' },
	start_form('get', '/cgi-bin/action.pl')
	. "<input type=hidden name=action value=set_maxratio>"
	. input({ -type => 'hidden', -name => 'id', -value => $r->{id} })
	. "<input type=text name=maxratio value=$r->{maxratio} style='width: 50px'>" 
	. submit({ -style => 'width: 30px', -value => 'OK' }) . endform);
    (my $up = $r->{up} ? ($r->{maxratio} ? '-' . fmsz(($r->{size} * $r->{maxratio} - $r->{up}) * (1 << 20)) : fmsz($r->{up} * (1 << 20))) : '--') =~ s/^--(.)/+$1/g;
    push @torrents, {
	id => $r->{id},
	active => $r->{active},
	error => $r->{error},
	done => $r->{done},
	icons => $statusimg,
	name => $bt->{name},
	files => $files,
	size => $r->{size} ? fmsz($r->{size} * (1 << 20)) : '--',
	up => $up,
	down => fmsz($r->{down} * (1 << 20)),
	ratio => $ratio,
	uprate => fmsz($r->{uprate}),
	downrate => fmsz($r->{downrate}),
	status => progressbar($r->{progress}, $r->{eta}),
    };
}
$total->{active} ||= 0;

my @pb = df '/var/cache/webtornado/users';

my $tmpl = new HTML::Template(
    filename => '/usr/share/webtornado/tmpl/list.tmpl', 
    die_on_bad_params => 0, 
    vanguard_compatibility_mode => 1,
    loop_context_vars => 1,
);
$tmpl->param({
    disk_free => fmsz($pb[3] * (1 << 10)),
    disk_total => fmsz(($pb[2] + $pb[3]) * (1 << 10)),
    disk_progressbar => progressbar(int(100*$pb[2]/($pb[2] + $pb[3])), 0, '97%'),
    torrents => [@torrents],
    total_icons => "$total->{active} / $total->{count}",
    total_name => 'total',
    total_size => fmsz($total->{size} * (1 << 20)), 
    total_up => fmsz($total->{up} * (1 << 20)),     
    total_down => fmsz($total->{down} * (1 << 20)),
    total_ratio => ($total->{up} and $total->{down}) ? r10($total->{up} / $total->{down}) : '--',
    total_speed => (fmsz($total->{downrate}) or '0b') . ' / ' . (fmsz($total->{uprate}) or '0b'),
    total_status => progressbar($total->{has_undone} ? int(100 * $total->{progress} / ($total->{size} or 1)) : 100),
    version => $VER::VER,
});
print $tmpl->output;
