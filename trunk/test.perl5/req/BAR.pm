package BAR;

use warnings;
use strict;

sub bar {
	print "BAR::bar() #1\n";
	my $foo = new FOO();
	print "BAR::bar() #2\n";
}

1;