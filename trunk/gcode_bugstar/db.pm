package db;

use strict;

use config;
use DBI;

my $dbh;
db::connect();

sub connect {
	warn sprintf '# connecting to mysql://%s@%s/%s',
		$conf->{dbuser},
		$conf->{dbhost},
		$conf->{dbname};
	$dbh = DBI->connect(
		sprintf(
			'dbi:mysql:%s;host=%s',
			$conf->{dbname},
			$conf->{dbhost},
		),
		$conf->{dbuser},
		$conf->{dbpass},
		{
			RaiseError => 1,
			PrintError => 0,
		},
	);
	return undef;
}

sub try(&) {
	my ($sub) = @_;
	my $r = eval { &$sub };
	if ($@ =~ m|^DBD::mysql::db do failed: MySQL server has gone away|) {
		db::connect;
		return db::try($sub);
	} elsif ($@) {
		die $@;
	}
	return $r;
}

sub do {
	my @p = @_;
	try {
		warn sprintf '# db::do(%s)', $p[0];
		$dbh->do(@p);
	};
}

sub prepare {
	my @p = @_;
	try {
		warn sprintf '# db::prepare(%s)', $p[0];
		$dbh->prepare(@p);
	};
}

sub selectall_hashref {
	my @p = @_;
	try {
		warn sprintf '# db::selectall_hashref(%s)', $p[0];
		$dbh->selectall_hashref(@p);
	};
}

sub selectrow_arrayref {
	my @p = @_;
	try {
		warn sprintf '# db::selectrow_arrayref(%s)', join ', ', @p;
		$dbh->selectrow_arrayref(@p);
	};
}

sub selectrow_hashref {
	my @p = @_;
	try {
		warn sprintf '# db::selectrow_hashref(%s)', join ', ', @p;
		$dbh->selectrow_hashref(@p);
	};
}

sub foreach {
	my ($qw, $cb, @bp) = @_;
	my $sth;
	try {
		$sth = db::prepare($qw); #;
		$sth->execute(@bp);
	};
	while (my $r = $sth->fetchrow_hashref) {
		local $_ = $r;
		&$cb;
	}
}

sub last_insert_id {
	db::selectrow_arrayref('SELECT LAST_INSERT_ID()')->[0];
}

sub found_rows {
	db::selectrow_arrayref('SELECT FOUND_ROWS()')->[0];
}

1
