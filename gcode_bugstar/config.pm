package config;

use strict;
use Exporter;

our @ISA = qw/Exporter/;
our @EXPORT = qw/$conf $utypes $ses/;

our $conf = {
	dbhost => '',
	dbname => '',
	dbuser => '',
	dbpass => '',
};

1;

