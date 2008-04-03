package WWW::FreeProxy::Look;

use LWP::Simple;

sub fetch {
	my ($self, $callback) = @_;
	my @lines = split /\n/, get('http://www.loo-k.info/plist.txt');
	shift @lines for 1..2;
	&$callback($_) for @lines;
}

1;
