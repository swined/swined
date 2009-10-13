package tiesub;

use warnings;
use strict;

our $return;

sub TIESCALAR {
	my ($class, $ref1, $ref2) = @_;
	print "TIESCALAR()\n";
	return bless { ref1 => $ref1, ref2 => $ref2 }, $class;
}

sub FETCH {
	my ($self) = @_;
	print "FETCH()\n";
	return sub { 
		return if $tiesub::return;
		local $tiesub::return = 1;
		&{$self->{ref1}}(@_);
		&{$self->{ref2}}(@_); 
	};
}

sub STORE {
	my ($self, $ref) = @_;
	print "STORE()\n";
	$self->{ref2} = $ref;
}

1;
