package Tayra::DBI;

use warnings;
use strict;
use base 'Class::DBI';

__PACKAGE__->connection('dbi:SQLite:dbname=../db.sqlite');
