#!/usr/bin/perl

use warnings;
use strict;
use Attribute::Args;
use web;
use config;
use SQL::Builder;

SQL::Builder::insert(
	'releases',
	major => web::params->{major},
	minor => web::params->{minor},
	type => web::params->{type},
	location => web::params->{location},
)->do(config::dbh);
web::redirect('list.pl');