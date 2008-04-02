package WWW::FreeProxy;

BEGIN {
	foreach my $dir (@INC) {
		next unless -d ($dir .= '/WWW/FreeProxy');
		opendir DIR, $dir;
		map { require $_ } grep /\.pmc?$/, readdir DIR;
		closedir DIR;
	}
}

1;