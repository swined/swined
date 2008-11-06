use strict;
use Qt;
use MainWin;
use Audio::MPD;

my $a = Qt::Application([@ARGV]);
(my $m = MainWin)->show;
$a->setMainWidget($m);
my $mpd = new Audio::MPD;

*MainWin::next = sub {
    $mpd->next;
};

*MainWin::prev = sub {
    $mpd->prev;
};

*MainWin::play = sub {
    $mpd->play;
};

exit $a->exec;