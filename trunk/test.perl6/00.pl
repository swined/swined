#!/usr/local/bin/perl6

class A {
	method foo(Int $x) {
		'A.foo()'.say;
	}
}

class B is A {
	method foo(Int $x) {
		'B.foo()'.say;
	}
	method bar(Int $x) {
		'B.bar()'.say;
		self.foo($x);
	}
}

my B $b .= new;
my A $a .= new;
