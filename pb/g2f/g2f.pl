#!/usr/bin/perl

use warnings;
use strict;
use Data::Dumper;
use WebService::Google::Reader;

my $reader = new WebService::Google::Reader(
	username => shift,
	password => shift
) or die;

my $feed = $reader->starred(count => 1) or die 'failed to retrieve feed';
for my $entry ($feed->entries) {
	printf Data::Dumper::Dump($entry);
}
