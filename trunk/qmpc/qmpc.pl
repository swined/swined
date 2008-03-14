use strict;
use Qt;
use MainWin;
use PropsWin;
use AboutBox;

my $a = Qt::Application([@ARGV]);
(my $m = MainWin)->show;
$a->setMainWidget($m);




exit $a->exec;