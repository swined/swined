#!/usr/bin/perl

use warnings;
use strict;
use web;

web::xslt '/create.xsl', { location => web::params->{location} };
