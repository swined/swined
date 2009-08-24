package args;

use warnings;
use strict;
use B::Generate;
no strict 'refs';

sub fix {
	my $caller = shift || caller;
	foreach my $method (keys %{"${caller}::"}) {
 		my $cv = B::svref_2object(\&{*{"${caller}::${method}"}});
		if($cv->FLAGS & B::SVf_POK && $cv->PV ne ';@') {
			printf "proto: %s\n", $cv->PV;
			$cv->PV(';@');
		}
	}
}

1
