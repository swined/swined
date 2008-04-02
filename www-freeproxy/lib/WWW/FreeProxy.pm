package WWW::FreeProxy;

use Exporter;

our @ISA = qw/Exporter/;
our @EXPORT = qw/fetch_proxies/;

BEGIN {
	foreach my $dir (@INC) {
		next unless -d ($dir .= '/WWW/FreeProxy');
		opendir $dir, $dir;
		map { require $_ } grep /\.pmc?$/, readdir $dir;
		closedir $dir;
	}
}

sub plugins { grep s/::$//, keys %WWW::FreeProxy:: }

sub fetch_proxies(&) { 
	my $callback = shift;
	$_->fetch($callback) for grep s/^/WWW::FreeProxy::/, plugins;
}

1;