#!/usr/bin/perl

sub xdie($) { 
	my $err = shift;
	$err =~ s/'/"/g; # "'
	`Xdialog --infobox '$err' 0 0 30000`;
	die "ERROR: $err\n";
}

sub getIf() {
	my %ifs = map { $_ => 1 } grep s/^\s*(\S+):.*$/$1/, grep s/[\r\n]//g, 
		`cat /proc/net/wireless 2> /dev/null`;
	xdie 'No wireless interfaces were found!' unless scalar keys %ifs;
	my $ifl = join ' ', keys %ifs;
	return $ifl if 1 == scalar keys %ifs;
	(my $if = scalar `Xdialog --stdout --combobox 'Wireless interface:' 0 0 $ifl`) 
		=~ s/[\r\n]//g;
	$ifs{$if} ? $if : xdie 'No wireless interface selected!';
}

sub getNet($) {
    my $if = shift;
    print `kdesu -t 'iwlist $if scan' | grep "^\\\\s*ESSID:" | sed -r "s/^.*\\"(.+?)\\".*\$/\\1/" | sort -u`;
}

my $if = getIf;
my $net = getNet $if;
