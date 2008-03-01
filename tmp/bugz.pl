#!/usr/bin/perl

use WWW::Bugzilla3;

my ($l, $p) = ('aalexandrov@swsoft.com', '');

sub pbl($@) {
	my ($n, @l) = @_;
	print "$n: " . (join ', ', sort @l) . "\n" if @l;
}

my $bz = new WWW::Bugzilla3(site => 'pembugs');
$bz->login($l, $p);

my ($sd, $ed) = ('2007-07-09', '2007-07-10');

my @bnew = $bz->search(
	reporter => $l, 
	chfieldfrom => $sd, 
	chfieldto => $ed, 
	chfield => '[Bug creation]');
my @bvrf = $bz->search(
        emailtype1 => 'exact',
        email1 => $l,
        emaillongdesc1 => 1,
        chfieldfrom => $sd,
	chfieldto => $ed,
	chfield => 'bug_status',
	chfieldvalue => 'VERIFIED');

pbl 'new', @bnew;
pbl 'vrf', @bvrf;
