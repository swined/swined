#!/usr/bin/perl

use warnings;
use strict;
use Attribute::Args;
use web;
use config;
use SQL::Builder;

SQL::Builder::delete('releases', id => web::params->{id})->do(config::dbh);
web::redirect('list.pl');
