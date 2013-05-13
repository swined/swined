#!/usr/bin/perl

use warnings;
use strict;
use Attribute::Args;
use web;
use config;
use SQL::Builder;
use Data::Dumper;

web::xslt '/list.xsl', { release => [ SQL::Builder::select->from('releases')->map(config::dbh) ] };
