#!/usr/bin/perl

use Getopt::Long;
use WWW::Bugzilla3;

GetOptions(
	my $o = {}, 
	'login=s', 'password=s', 'site=s', 'product=i',	
	'maxbugs=i', 'verify',
);

sub get_dups { shift->search(product => shift, bug_status => 'RESOLVED', resolution => 'DUPLICATE') }

sub get_dup_origin {
	my ($bz, $id) = @_;
	my $rs = $bz->ua->get("$bz->{site}show_bug.cgi?id=$id")->content;
	return $1 if $rs =~ /of bug <span class="bz_closed"><a href="show_bug\.cgi\?id=(\d+)" title="VERIFIED/ms;
	return 0;
}

sub verify {
	my ($bz, $id, $cm) = @_;
	
}

my $bz = new WWW::Bugzilla3(site => $o->{site});
$bz->login($o->{login}, $o->{password});
my @products = $bz->get_products($bz->get_selectable_products);

my ($tbc, @bugs) = (0);

foreach (@products) {
	next unless 
		($_->{id} == $o->{product}) 
		or (!defined $o->{product});
	my $pn = $_->{name};
	my $bc = 0;
	foreach ($bz->get_bugs(get_dups($bz, $pn))) {
		next if 
			($tbc >= $o->{maxbugs}) 
			and (defined $o->{maxbugs});
		my $orig = get_dup_origin($bz, $_->{id});
		next unless $orig;
		print "[$pn]\n" unless $bc++;
		$tbc++;
		push @bugs, $_->{id};
		print "$_->{id} -> $orig\t$_->{summary}\n";
	}
}

if ($o->{verify}) {
	print "\nverifying: ";
	verify($bz, $_, 'vrf as duplicate') and print "$_ " for @bugs;
	print "\n";
}
