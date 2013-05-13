package config;

use warnings;
use strict;
use Attribute::Args;
use DBI;
use Cwd;

my $dbh = DBI->connect('dbi:SQLite:dbname=../db.sqlite');

sub dbh :ARGS { $dbh }
sub types :ARGS { ('DEBUG_SHELL', 'RELEASE_SHELL', 'DEBUG_DB', 'RELEASE_DB') }
sub uploadPath :ARGS { Cwd::abs_path('../htdocs/updates') }
sub uploadUrl :ARGS { '/updates/' }

1;
