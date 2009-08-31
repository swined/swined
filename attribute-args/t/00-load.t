#!perl -T

use Test::More tests => 1;

BEGIN {
	use_ok( 'Attribute::Args' );
}

diag( "Testing Attribute::Args $Attribute::Args::VERSION, Perl $], $^X" );
