#!/usr/bin/perl

use warnings;
use strict;
use tiesub;

my $foo = sub { print "foo\n" };
my $bar = sub { print "bar\n" };

tie $foo, 'tiesub', $bar, $foo;

&$foo();
