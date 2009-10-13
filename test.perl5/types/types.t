#!/usr/bin/perl

use warnings;
use strict;
use Hook::WrapSub;

sub foo {
	print "foo()\n";
}

foo;
foo;
