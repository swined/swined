package proto;

use warnings;
use strict;
use B::Generate;
use Attribute::Args;

my @modules;

sub import {
	my $caller = caller;
	push @modules, $caller;
}

sub fix_sub :ARGS('scalar', 'scalar') {
	my ($package, $method) = @_;
	my $symbol = "${package}::${method}";
	no strict 'refs';
	my $ref = \&{*$symbol};
	my $cv = B::svref_2object(*$symbol{CODE});
	if($cv->FLAGS & B::SVf_POK) {
		my $sig = $cv->PV;
		$cv->PV(';@');
		my $data = [ split /,/, $sig ];
		no warnings;
		*$symbol = sub { return Attribute::Args::wrapper($data, \*$symbol, $ref, @_) };
	}
}

sub fix :ARGS('scalar?') {
	my $caller = shift || caller;
	no strict 'refs';
	proto::fix_sub($caller, $_) for keys %{"${caller}::"};
}

INIT { proto::fix($_) for @modules }

1;
