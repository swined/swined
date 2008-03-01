#!/usr/bin/perl

use Mail::Webmail::Gmail;

my $gmail = new Mail::Webmail::Gmail(username => shift, password => shift);

foreach (@{$gmail->get_messages(label => 'unread')}) {
	my %lab = map { $_ => 1 } @{$_->{labels}};
	next if $_->{starred} or $lab->{'^i'};
	print "$_->{id} $_->{subject} " . join(', ', @{$_->{labels}}) . "\n";
}

