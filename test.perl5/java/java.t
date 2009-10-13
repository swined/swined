#!/usr/bin/perl

use warnings;
use strict;
use Java;

my $java = new Java;
my $list = $java->create_object('java.util.LinkedList');

$list->add($_) for reverse 1 .. 10;
my $iterator = $list->iterator;
while (my $item = $iterator->next) {
	printf "%s\n", $item->get_value;
}
