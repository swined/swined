#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use CGI qw/:all/;
use CGI::Debug;
use WT;
use HTML::Widgets::Table;
use Filesys::DiskSpace;
use URI::Escape;

my $wt = new WT;

print $wt->cgi->header(-content_type => 'text/html; charset=utf-8');

sub A { a({ -href => shift }, join ' ', @_) }
sub IMG { img({ -border => 0, -src => shift }) }

sub r10 { int(10 * (shift or $_)) / 10 }

sub fmsz {
    my $s = shift;
    return r10 . 'T' if 0.7 < abs(local $_ = $s/1024/1024/1024/1024);
    return r10 . 'G' if 0.7 < abs(local $_ = $s/1024/1024/1024);
    return r10 . 'M' if 0.7 < abs(local $_ = $s/1024/1024);    
    return r10 . 'k' if 0.7 < abs(local $_ = $s/1024);    
    return int($s) . 'b';
}

sub progressbar {
    my ($p, $e, $w) = @_;
    return "unknown" unless $p;    
    return "done" if $p >= 100;
    my @E = gmtime($e);
    my $eta = "@E[1]:@E[0]";
    --@E[3];
    $eta = "@E[2]:$eta" if @E[2];
    $eta = "@E[3]d $eta" if @E[3];
    $eta = '> 1 week' if @E[3] > 6;
    $eta = $e ? "eta $eta" : '';
    $eta =~ s/:(\d)(?!\d)/:0$1/g;
    $w = '100px' unless $w;
    $p .= '%' unless $p =~ /%$/;
    center($eta . div({ -style => 
	"border: 1px solid black; width: $w; height: 5px; background-color: black; text-align: left"
	}, div({ -style => "width: ${p}; height: 100%; background-color: #00FF00" })));
}

print meta({ -http_equiv => 'REFRESH', -content => '30;URL=/' });
print "<center>";

my $total = {};
my $table = new HTML::Widgets::Table({ 
    alternating_row_colors => ['#ffffff', '#f2f2f2'], 
    style => 'width: 100%',
});
$table->addHeaderRow(['&nbsp;', 'name', 'size', 'up', 'down', 'ratio', 'speed', 'status'], { style => 'background-color: #eeeeee' });
my $sth = $wt->dbh->prepare('SELECT *,100*up/progress/size AS ratio,progress*size/100 AS down
    FROM torrents WHERE owner = ? ORDER BY up/size DESC');
$sth->execute($ENV{REMOTE_USER});
while (my $r = $sth->fetchrow_hashref) {
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
    my $name = (-e $r->{output}) ? $r->{output} : $r->{filename};
    $name =~ s{.+/}{};
    my $speed = ($r->{done} ? '' : fmsz($r->{downrate}) . ' / ') . fmsz($r->{uprate});
    $speed = 'stalled' unless $r->{uprate} or $r->{downrate};
    $statusimg .= A("/$r->{id}.tar", IMG('/img/tar_down.gif')) if $r->{done} and not $r->{del};
    $statusimg .= A("/delete/$r->{id}", IMG('/img/delete.png')) unless $r->{del};
    my $VAR1;
    eval {
	local $SIG{__DIE__} = sub {
	    $r->{error} = shift;
	    $dbh->do('UPDATE torrents SET torrent = NULL WHERE id = ?', undef, $r->{id});
	};
	$VAR1 = WT::getTorrentInfo(uri_unescape $r->{torrent});
    };
    #eval $r->{info};
    my $files = join '<br>', map { '[' . fmsz($_->{size}) . '] ' . $_->{name} } @{$VAR1->{files}};
    my $fc = scalar @{$VAR1->{files}};
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
    my $up = $r->{up} ? ($r->{maxratio} ? '-' . fmsz($r->{size} * $r->{maxratio} * 1024 * 1024 - $r->{up} * 1024 * 1024) : fmsz($r->{up} * 1024 * 1024)) : '--';
    $up =~ s/^--(.)/+$1/g;
    my $err = $r->{error} ? "<br><font color=red>$r->{error}</font>" : "";
    $table->addRow([
	$statusimg,
	div({ -style => 'text-align: left' }, $name, $files, $err),
	$r->{size} ? fmsz($r->{size} * 1024 * 1024) : '--', 
	$up,
	fmsz($r->{down} * (1 << 20)),
	"<nobr>$ratio</nobr>",
	$r->{active} ? $speed : '--',
	progressbar($r->{progress}, $r->{eta}), 	
    ], { style => 'text-align: center', valign => 'top' });
}
$total->{active} |= 0;
$table->addRow([
    "$total->{active} / $total->{count}",
    'total',
    fmsz($total->{size} * 1024 * 1024), 
    fmsz($total->{up} * 1024 * 1024),     
    fmsz($total->{down} * (1 << 20)),
    ($total->{up} and $total->{down}) ? r10($total->{up} / $total->{down}) : '--',
    fmsz($total->{downrate}) . ' / ' . fmsz($total->{uprate}),
    progressbar($total->{has_undone} ? int(100 * $total->{progress} / ($total->{size} or 1)) : 100),
], { style => 'text-align: center' });

my @pb = df '/var/cache/webtornado/users';
print 'diskspace: ' . fmsz($pb[3] * (1 << 10)) . ' of ' . fmsz(($pb[2] + $pb[3]) * (1 << 10)) . ' free';
print progressbar int(100*$pb[2]/($pb[2] + $pb[3])), 0, '97%';

print br . $table->render . br . 
    A('/start/all', 'start all') . ' | ' .
    A('/stop/all', 'stop all') . ' | ' .
    'add new torrent: ' . start_form('post', '/upload') . 
    filefield('file') . '&nbsp;' . submit . endform;

print "<font color=gray>webtornado 0.0.6 &copy; swined</font>";
print "</center>";
