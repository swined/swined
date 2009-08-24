#!/usr/bin/perl

use B;
use B::Generate;
use B::Concise;

sub op_dump {
	my ($op) = @_;
	print "+++\n";
	while ($op and not $op->isa('B::NULL')) {
		if ($op->isa('B::SVOP')) {
			printf "%s: %s/%s (%s) sv=%s\n", B::class($op), $op->name, $op->type, $op->desc, $op->sv->sv;
		} else {
			printf "%s: %s/%s (%s)\n", B::class($op), $op->name, $op->type, $op->desc;
		}
		$op->dump;
		$op = $op->next;
	}
	print "---\n";
}

sub foo {
	29;
}

my $b = B::svref_2object(\&foo);
B::Concise::concise_cv_obj('basic', $b);
