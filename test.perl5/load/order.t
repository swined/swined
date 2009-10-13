#!/usr/bin/perl

use warnings;
use strict;
use order;
use POSIX;
use Attribute::Handlers;

sub FOO :ATTR {
}

sub ARGS :ATTR {
}

sub foo :FOO :ARGS {
}
