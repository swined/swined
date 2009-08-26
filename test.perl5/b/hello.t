#!/usr/bin/perl

use B;
use B::Generate;

sub op_dump {
	my ($op, $cv, $i) = @_;
	printf "%s %s/%s\n", ('-' x $i), B::class($op), $op->name;
	if ($op->isa('B::SVOP')) {
	#	printf "sv=%s\n", $op->sv->sv;
	} elsif ($op->isa('B::PADOP')) {
		printf "padval[%s]=%s\n", $op->padix, $op->padval($cv);
	}
	op_dump($op->next, $cv, $i) unless $op->next->isa('B::NULL');
}

sub B::PADOP::padval {
        my ($self, $cv) = @_;
        my ($names, $values) = $cv->PADLIST->ARRAY;
        my @v = $values->ARRAY;
	my $r = $v[$self->padix];
        return $r->sv;
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
	print "baz()\n"; 
}

sub bar {
	print "bar()\n";
}

sub foo {
#	print "foo()\n";
	bar;
	my $sub = sub { print "anon()\n" };
	&$sub();
}

print \&baz;

my $b = B::svref_2object(\&foo);
cv_dump $b;
my $gv = new B::PADOP('gv', 0);
B::PADOP::padix($gv, 2);
op_insert $b->START, $gv, new B::UNOP('entersub', 0, 0);
cv_dump $b;
foo;
