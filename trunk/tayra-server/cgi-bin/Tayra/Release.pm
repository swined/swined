package Tayra::Release;

use warnings;
use strict;
use base 'Tayra::DBI';

__PACKAGE__->table('releases');
__PACKAGE__->columns(All => qw/major minor type location/);

1;
