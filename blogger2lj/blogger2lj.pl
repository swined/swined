#!/usr/bin/perl

use Config::File;

my $c = Config::File::read_config_file('blogger2lj.conf');
local $\ = "\n";
print $c->{blogger_url};