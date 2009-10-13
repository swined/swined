package order;

use warnings;
use strict;
use attributes;
no strict 'refs';
use B;

my %packages;

sub import {
	my ($module, @args) = @_;
	my @caller = caller;
	$packages{$caller[0]} = $caller[1] unless grep { $_ eq 'noforce' } @args;
}

sub CHECK {
	foreach my $package (keys %packages) {
		my %pkg = %{"${package}::"};
		foreach my $sub (map { "${package}::${_}" } keys %pkg) {
			next unless UNIVERSAL::can($package, $sub);	
			my $ref = \&{*$sub};
			my $b = B::svref_2object($ref);
			next if $b->FILE ne $packages{$package};
			next if grep /^_ATTR_\W+_ATTR$/, attributes::get($ref);
			die sprintf ':ARGS() is required at %s', $sub unless grep /^_ATTR_\W+_ARGS$/, attributes::get($ref);
			printf "%s::%s\n", $package, $sub;
		}
	}
}

1;
