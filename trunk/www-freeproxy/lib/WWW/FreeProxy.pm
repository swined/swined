package WWW::FreeProxy;

BEGIN {
	foreach my $dir (@INC) {
		next unless -d ($dir .= '/WWW/FreeProxy');
		opendir $dir, $dir;
		map { require $_ } grep /\.pmc?$/, readdir $dir;
		closedir $dir;
	}
}

sub plugins { grep s/::$//, keys %WWW::FreeProxy:: }

1;