use strict;
use Qt;
use MainWin;
use Audio::MPD;

my $a = Qt::Application([@ARGV]);
(my $m = MainWin)->show;
$a->setMainWidget($m);
my $mpd = new Audio::MPD;

*MainWin::mpdNextAction_activated = sub {
    $mpd->next;
};

exit $a->exec;