package o;

use overload '%{}' => 'toHash';

sub new {
	my ($class) = @_;
	my $ref = {
		'private' => 'value',
	};
	return bless $ref, $class;
}

sub toHash :lvalue {
	my ($self, @args) = @_;
	printf "%s\n", join ', ', @args;
	return {
		'no' => 'way',
	};
}

1;
