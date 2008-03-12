use strict;
use Qt;
use MainWin;
use PropsWin;
use AboutBox;

my $a = Qt::Application([@ARGV]);
(my $m = MainWin)->show;
$a->setMainWidget($m);

*MainWin::fileExit = sub {
    $a->exit;
};

*MainWin::helpAbout = sub {
    AboutBox($m)->show;
};

exit $a->exec;