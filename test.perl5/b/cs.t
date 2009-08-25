#!/usr/bin/perl

use cs;

sub baz {
	print "baz()\n";
}

sub bar {
	print "bar()\n";
}

sub foo {
	my $bar = 'baz';
	print "foo()\n";
}

my $i = 0;
cs::inject(
	code => sub { main::bar(); }, 
	target => \&foo,
	precondition => sub { 
		my $op = shift; 
		return $i++ > 4;
#		$op->name eq 'const';
	},
);

foo;
