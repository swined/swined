package WT;

use lib '/usr/share/webtornado/pm';
use CGI;
use DBI;
use Bencode;
use Config::File;
use DBIx::DBSchema::Column;
use DBIx::DBSchema::Table;

sub new {
    my ($c, %p) = @_;
    $p{conffile} |= '/etc/webtornado.conf';
    $p{conf} = Config::File::read_config_file($p{conffile});
    my $dsn = "DBI:mysql:database=$p{conf}->{dbname}:host=$p{conf}->{dbhost}";
    $p{dbh} = DBI->connect($dsn, $p{conf}->{dbuser}, $p{conf}->{dbpass}); 
    $p{cgi} = new CGI;
    bless \%p;
}

# ===

sub conf { shift->{conf} }
sub dbh { shift->{dbh} }
sub cgi { shift->{cgi} }

# ===

sub do {
    shift->dbh->do(@_);
}

sub selectrow_hashref {
    shift->dbh->selectrow_hashref(@_);
}

sub selectall_arrayref {
    shift->dbh->selectall_arrayref(@_);
}

# ===

sub cat {
	open F, '< ' . shift;
	my $r;
	$r .= $_ while <F>;
	close F;
	return $r;
}

sub shesc {  
    local $_ = shift, s/['\\]/\\$&/g; #'
    return "'$_'";
}

sub getTorrentInfo {
	my $i = Bencode::bdecode(shift)->{'info'};
	my $r = { 'name' => $i->{'name'}, 'files' => [] };
	push @{$r->{'files'}}, { 'size' => $i->{'length'}, 
		'name' => $i->{'name'} } unless defined $i->{'files'};
	foreach (@{$i->{'files'}}) {
		$_->{'path'} = $_->{'path'}->[0] if 'ARRAY' eq ref $_->{'path'};
		push @{$r->{'files'}}, {
			'name' => "$i->{name}/$_->{path}",
			'size' => $_->{'length'},
		};
	}
	$r->{'total_size'} += $_->{'size'} for @{$r->{'files'}};
	return $r;
}

sub syncdb {
    my $dbh = shift->dbh;
    my $cur_table = new_odbc DBIx::DBSchema::Table($dbh, 'torrents');
    my $req_table = new DBIx::DBSchema::Table({
	name => 'torrents', 
	columns => [
	    new DBIx::DBSchema::Column('id', 'int', 0, 11, undef, 'auto_increment'),
	    new DBIx::DBSchema::Column('pid', 'int', 1),
	    new DBIx::DBSchema::Column('active', 'int', 1),
	    new DBIx::DBSchema::Column('del', 'int', 1),
	    new DBIx::DBSchema::Column('owner', 'text', 1), 
	    new DBIx::DBSchema::Column('filename', 'text', 1),
	    new DBIx::DBSchema::Column('outdir', 'text', 1),
	    new DBIx::DBSchema::Column('output', 'text', 1),  
	    new DBIx::DBSchema::Column('size', 'double', 1),
	    new DBIx::DBSchema::Column('up', 'double', 1), 
	    new DBIx::DBSchema::Column('error', 'int', 1),
	    new DBIx::DBSchema::Column('progress', 'int', 1),
	    new DBIx::DBSchema::Column('downrate', 'int', 1),
	    new DBIx::DBSchema::Column('uprate', 'int', 1),
	    new DBIx::DBSchema::Column('eta', 'int', 1),
	    new DBIx::DBSchema::Column('info', 'text', 1),
	    new DBIx::DBSchema::Column('maxratio', 'double', 1),
	    new DBIx::DBSchema::Column('torrent', 'text', 1), 
	],
    });
    $dbh->do($_) for $cur_table->sql_alter_table($req_table, $dbh);
}

1
