package WWW::FreeProxy::Test;

sub fetch {
	my ($self, $callback) = @_;
	&$callback('127.0.0.1:3128');
	&$callback('127.0.0.1:8080');
}

1;