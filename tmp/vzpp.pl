#!/usr/bin/perl

use Net::VZ;

foreach (['10.63.53.56', ''], ['swd.pp.ru', '', 23]) {
	print "# @$_[0]\n";
	my $vz = new Net::VZ(host => @$_[0], pass => @$_[1], port => @$_[2]);
	print "$_\n" for $vz->vzlist;
}
