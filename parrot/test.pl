#!/usr/local/bin/perl6

class TestClass {
	method test {
		say "TestClass.test()";
		.prop = 1;
	}
}

my TestClass $test .= new;
$test.test;
