#!/usr/bin/perl

use B;
use B::Generate;
use B::Utils;

sub op_dump {
	my ($op, $cv, $i) = @_;
	printf "%s %s/%s\n", ('-' x $i), B::class($op), $op->name;
	if ($op->isa('B::SVOP')) {
		printf "sv=%s\n", $op->sv->sv;
	} elsif ($op->isa('B::PADOP')) {
		printf "padix=%s\n", $op->padix;
	}
	op_dump($op->next, $cv, $i) unless $op->next->isa('B::NULL');
}

sub cv_dump {
	my ($cv) = @_;
	print "+++++++++++++++++\n";
	op_dump($cv->START, $cv, 1);
	print "-----------------\n";
}

sub op_insert {
	my ($target, $op, @ops) = @_;
	$op->next($target->next);
	$target->next($op);
	main::op_insert($op, @ops) if @ops;
	return;
}

sub baz {
	printf "baz()\n";
}

sub bar {
	printf "bar(%s)\n", join ', ', @_;
	return 29;
}

sub foo {
	my $x = 'bar';
	bar 'baz';
}

my $b = B::svref_2object(\&foo);
cv_dump $b;
foo;
op_insert 
	$b->START->next,
	new B::UNOP('entersub', 0, 0),
	new B::OP('leavesub', 0),
;
cv_dump $b;
foo;
