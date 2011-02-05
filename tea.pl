#!/usr/bin/perl

use warnings;
use strict;

my $cards = {
	'2' => 'two is for you',
	'3' => 'three is for me',
	'4' => 'four - touch the floor',
	'5' => 'ladies drink',
	'6' => 'gentlemen drink',
	'7' => 'heaven',
	'8' => 'pick a date',
	'9' => 'basta rhyme',
	'10' => 'i\'ll never do that again',
	'J' => 'category',
	'Q' => 'question',
#	'K' => '',
	'A' => 'rule',
};

sub deck {
    my @deck = ();
    push @deck, keys %$cards for 1..4;
    for (1..100) {
	if (rand() > 0.5) {
	    my $t = $deck[$_ % scalar @deck];
	    $deck[$_ % scalar @deck] = $deck[($_ + 1) % scalar @deck];
	    $deck[($_ + 1) % scalar @deck] = $t;
	}
    }
    return @deck;
}

for (deck()) {
    printf "%s\n%s\n", $_, $cards->{$_};
    <>;
}
