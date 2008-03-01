package PEM::API;

use XML::Simple;
use XMLRPC::Lite;

sub new {
	my ($c, %p) = @_;
	$p{url} = "http://$p{mn}:8440/" if $p{mn};
	$p{rpc} = XMLRPC::Lite->proxy($p{url});
	bless \%p;
}

sub call { 
	my $r = shift->{rpc}->call(shift, shift)->result;
	$r->{status} ? die $r->{error_message} : $r;
}

sub AUTOLOAD {
	(my $n = $AUTOLOAD) =~ s/.*:_?(?{y,_,.,})//;
	my %f = %{ XMLin([ grep s/API://g, @_[0]->call('pem.getMethodSignature', { method_name => $n })->{signature} ]->[0], forceArray => 1)->{FIELD} };
	my @m = sort grep { ! $f{$_}->{OPTIONAL} and ! $f{$_}->{STRUCT} } keys %f;
	&{*$AUTOLOAD = sub { shift->call($n, { map { ref eq 'HASH' ? %$_ : shift @m => $_ } @_ })->{result} }}(@_);
}

1
