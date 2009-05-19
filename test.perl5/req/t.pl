#!/usr/bin/perl

use warnings;
use strict;

sub foo {
#	require FOO;
	require BAR;
	eval { BAR::bar() };
}

foo();