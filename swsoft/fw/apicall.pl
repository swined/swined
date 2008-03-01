#!/usr/bin/perl

use Getopt::Long;
use PEM::API;

GetOptions(my $o = {}, 'node=s', 'method=s');
my $met = $o->{method};
print PEM::API->new(mn => $o->{node})->$met(@ARGV);
print "\n\n";
