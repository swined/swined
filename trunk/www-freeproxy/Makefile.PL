#!/usr/bin/perl

# $Id: Makefile.PL 23 2007-07-06 20:00:44Z swined $

use strict;
use warnings;
use ExtUtils::MakeMaker;

WriteMakefile(
    NAME                => 'WWW::FreeProxy',
    AUTHOR              => 'Alexey Alexandrov <swined@cpan.org>',
    VERSION_FROM        => 'lib/WWW/FreeProxy.pm',
    ABSTRACT_FROM       => 'lib/WWW/FreeProxy.pm',
    PL_FILES            => {},
    PREREQ_PM => {
	'LWP::Simple' => 0,
	'Exporter' => 0,
    },
    dist                => { COMPRESS => 'gzip -9f', SUFFIX => 'gz', },
    clean               => { FILES => 'WWW-FreeProxy-*' },
);
