#!/usr/bin/perl

use o;

my $o = new o();
#printf "%s = %s\n", $_, $o->{$_} for keys %$o;
$o->{x} = 'y';
