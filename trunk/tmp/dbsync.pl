#!/usr/bin/perl

use DBI;
use DBIx::DBSchema::Column;
use DBIx::DBSchema::Table;

sub stab {
    my ($dbh, $tab, @fld) = @_;
    my $req_table = new DBIx::DBSchema::Table({ name => $tab, columns => [ map { new DBIx::DBSchema::Column(@$_) } @fld ] });
    $dbh->do($_) for DBIx::DBSchema::Table($dbh, $tab)->new_odbc->sql_alter_table($req_table, $dbh);
}

stab $dbh, '', 

1
