#!/usr/bin/perl

use warnings;
use strict;
use Attribute::Handlers;

sub OVERRIDE :ATTR {
	my ($package, $symbol, $referent, $attr, $data, $phase, $filename, $linenum) = @_;
	my $original = \&{*$symbol};
	*$symbol = sub {
		my (@p) = @_;
		print "before\n";
		my $r = &$original(@p);
		print "after\n";
		return $r;
	};
}

sub foo :OVERRIDE {
	print "foo\n";
}

foo();