#!/usr/bin/perl

use strict;
use Net::NNTP;

local $\ = "\n";
my $n = new Net::NNTP('localhost:1119') or die 'failed to create Net::NNTP object';
print for keys %{$n->list};
