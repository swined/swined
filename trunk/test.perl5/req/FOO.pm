package FOO;

use warnings;
use strict;
use fields qw/foo bar/;

sub new {
	my ($class, %p) = @_;
	my $self = fields::new($class);
	$self->{$_} = $p{$_} for keys %p;
	return $self;
}

1;