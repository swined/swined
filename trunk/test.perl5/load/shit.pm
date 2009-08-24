package shit;

use strict;
use args;

sub foo (proto:foo) {
	printf "shit::foo()\n";
}

sub bar (proto:bar) {
	printf "shit::bar()\n";
}

sub baz () {
	printf "shit::baz()\n";
}

BEGIN { args::fix }

1;
