#!/usr/bin/perl

use warnings;
use strict;
use B::Deparse;
use B::Generate;

sub foo {
	printf "foo()\n";
}

sub inject {
	my ($sub, $code) = @_;
	my $deparse = new B::Deparse;
	my $sub_src = $deparse->coderef2text($sub);
	my $code_src = $deparse->coderef2text($code);
	my $sub_b = B::svref_2object($sub);
	my $code_b = B::svref_2object(sub {
		my $r = eval $code_src . $sub_src;
		die $@ if $@;
		return $r;
	});
	$sub_b->START($code_b->START);
}

inject \&foo, sub { print "injected()\n"; };
foo;
