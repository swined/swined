#!/usr/bin/perl

use warnings;
use strict;
use web;
use config;
use File::Temp;
use File::Copy;
use File::Basename;
use URI::Escape;

my $data = web::upload('file') || web::redirect('list.pl');
my $temp = File::Temp::tempnam(config::uploadPath, '');
copy($data, $temp) || warn(sprintf 'copy failed: %s', $!);
web::redirect('create.pl?location=' . uri_escape(config::uploadUrl . basename $temp));
