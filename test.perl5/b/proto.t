#!/usr/bin/perl

use B::Generate;

sub proto {
	my ($package, $method) = @_;
	my $cv = B::svref_2object(*{$package.'::'.$method}{CODE});
	if($cv->FLAGS & B::SVf_POK) {
		my $sig = $cv->PV;
		printf "%s::%s (%s)\n", $package, $method, $sig;
		$cv->PV(';@');
	} else {
		printf "shit happened: %s::%s\n", $package, $method;
	}
}

sub foo ($x = int, ?$y = WWW::Mechanize, ?%h,scalar) {
}

BEGIN {
	proto('main', 'foo');
}

foo;
