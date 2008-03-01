#!/usr/bin/perl

sub rn { grep s/[\r\n]//g, @_ }
sub pl { grep s/^ii +(\w+) .*$/$1/, rn `dpkg -l` }
sub pd { grep s/^(.*)(| \(.*\))/$1/, split ', ', join ', ', grep s/^Depends: //, rn `dpkg -s @_[0]` }

my ($pd, @pl) = ({}, pl);
foreach (@pl) { 
	print "\rChecking: $_" . (' ' x 20) . "\r"; 
	$pd->{$_}++ for pd $_; 
}
print "\r" . (' ' x 20) . "\r";
#print map { "$_\n" } grep { !$pd->{$_} } @pl;
print map { "$_\n" } keys %$pd;

