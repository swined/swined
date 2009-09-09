#!perl

use strict;
use Test::Strict;

$Test::Strict::DEVEL_COVER_OPTIONS = '-coverage,statement,branch,condition,path,subroutine,time';

all_cover_ok(100);
