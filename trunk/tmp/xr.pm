package xr;

use XMLRPC::Lite;
use Carp;
#use AutoLoader;

sub new {
    my ($c, %p) = @_;
    $p{rpc} = XMLRPC::Lite->proxy($p{url});
    bless \%p;
}

sub AUTOLOAD {
    my ($N) = @_;
    (my $n = $N) =~ s/^.*:://;
    $n =~ s/_/./g;
    print $N;
#    *$N = sub { shift->{rpc}->call($n, shift)->result };
}

1;