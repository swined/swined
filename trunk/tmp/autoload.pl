#!/usr/bin/perl

sub call {
	my ($mandatory, $optional) = (@_);
	$mandatory = [@_] unless ref $mandatory eq 'ARRAY';
	my %params = %$optional;
	my @mp = ('x', 'y', 'z');
	for (my $i = 0; defined @$mandatory[$i]; $i++) {
		$params{@mp[$i]} = @$mandatory[$i] if defined @mp[$i];
	}
	print join(', ', @$mandatory) . "\n";
	print join(', ', keys %$optional) . "\n";
	print map { "$_ -> $params{$_}\n" } keys %params;
}

call ['a', 0], { b => 'c'};
call 1, 2, 3, 4;
