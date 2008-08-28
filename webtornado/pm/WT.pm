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
	my $b = Bencode::bdecode(shift);
	my $i = $b->{'info'};
	my $r = { 'announce' => $b->{'announce'}, 'name' => $i->{'name'}, 'files' => [] };
	push @{$r->{'files'}}, { 'size' => $i->{'length'},
		'name' => $i->{'name'} } unless defined $i->{'files'};
	foreach (@{$i->{'files'}}) {
		$_->{'path'} = join '/', @{$_->{'path'}} if 'ARRAY' eq ref $_->{'path'};
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
    $dbh->do('CREATE TABLE IF NOT EXISTS torrents(id int primary key not null auto_increment)');
    my $cur_table = new_odbc DBIx::DBSchema::Table($dbh, 'torrents');
    my $req_table = new DBIx::DBSchema::Table({
	name => 'torrents',
	columns => [ map { new DBIx::DBSchema::Column(@$_) }
		['id', 'int', 0, 11, undef, 'auto_increment'],
		['pid', 'int', 0],
		['active', 'int', 0],
		['del', 'int', 0],
		['owner', 'text', 1],
		['outdir', 'text', 1],
		['output', 'text', 1],
		['up', 'double', 0],
		['down', 'double', 0],
		['error', 'text', 1],
		['progress', 'int', 1],
		['downrate', 'int', 1],
		['uprate', 'int', 1],
		['eta', 'int', 1],
		['maxratio', 'double', 1],
		['torrent', 'longtext', 1],
		['peers', 'int', 0],
		['peerlist', 'text', 0],
		['vmsize', 'int', 0],
		['vmrss', 'int', 0],
	],
    });
    $dbh->do($_) for $cur_table->sql_alter_table($req_table, $dbh);
}

1
