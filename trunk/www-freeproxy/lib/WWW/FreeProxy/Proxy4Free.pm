package WWW::FreeProxy::Proxy4Free;

use LWP::Simple;

sub fetch {
	my ($self, $callback) = @_;
	for (1..6) {	       
		my $content = get("http://www.proxy4free.com/page$_.html") or return [];
		&$callback("$1:$2") while $content =~ m~<td>(\d+\.\d+\.\d+\.\d+)</td>.*?<td>(\d+)</td>~igs;
	}
}

1;
