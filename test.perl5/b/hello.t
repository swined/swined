#!/usr/bin/perl

use B;
use B::Generate;

sub bar {
	my ($x, $p) = @_;
	printf "bar(%s)\n", join ', ', @$p;
}

sub foo {
	bar(\@_, ['1', '2', '3']);
	my ($x, $y, $z) = @_;
	printf "hello, world\n";
}

sub op_dump {
	my ($op) = @_;
	print "+++\n";
	while ($op = $op->next and not $op->isa('B::NULL')) {
		printf "%s: %s/%s (%s)\n", B::class($op), $op->name, $op->type, $op->desc;
	}
	print "---\n";
}

{
	my $b = B::svref_2object(\&foo);
	my $op = $b->START;
	my $r = new B::UNOP('entersub', 0, \&bar);
	op_dump $op;
	foo;
	$r->next($op->next);
	$op->next($r);
}
{
	my $b = B::svref_2object(\&foo);
	my $op = $b->START;
	op_dump $op;
	foo;
}
