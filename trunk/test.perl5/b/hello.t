#!/usr/bin/perl

use B;
use B::Generate;

sub wrap {
	my ($pre, $sub) = @_;
	my $b_pre = B::svref_2object(\&$pre);
	my $b_sub = B::svref_2object(\&$sub);
	my ($cur, $last) = $b_pre->START;
	$last = $cur while ($cur = $cur->next and not $cur->isa('B::NULL'));
	$last->next($b_sub->START->next || die);
	$b_sub->START->next($b_pre->START || die);
}

sub temp {
	printf "world, hello\n";
}

sub foo {
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

#wrap \&temp, \&foo;
#op_dump \&foo;

{
	my $b = B::svref_2object(\&foo);
	my $op = $b->START;
	my $r = new B::OP('leavesub', 0);
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
