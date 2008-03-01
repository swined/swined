#!/usr/bin/perl

my $skip = join ' ', map { "-not -path '$_/*'" } (
	'/proc', 
	'/root', 
	'/tmp',
	'/var/run',
	'/var/log',
	'/var/lib/dpkg',
	'/etc/ssl/certs',
);

my %pkg = map { $_ => 1 } grep s/[\r\n]//g, `cat /var/lib/dpkg/info/*.list`; 
print "$_\n" for grep { !$pkg{$_} } grep s/[\r\n]//g, `find / -xdev $skip`;
