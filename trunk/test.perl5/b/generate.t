#!/usr/bin/perl

use warnings;
use strict;
use B;
use B::Generate;

sub foo {
	my ($bar, $baz) = @_;
	printf "%s, %s\n", $bar, $baz;
}

sub bar {
	foo(\@_, ['1', '2', '3']);
	printf "hello world\n";
}

sub printsub {
	my ($sub) = @_;
	printf "---\n";
	my $b = B::svref_2object($sub);
	my $op = $b->START;
	while ($op = $op->next and not $op->isa('B::NULL')) {
#		printf "%s: %s/%s (%s)\n", B::class($op), $op->name, $op->type, $op->desc;
		print $op->dump;
	}
}

printsub \&foo;
printsub \&bar;
