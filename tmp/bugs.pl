#!/usr/bin/perl

use WWW::Bugzilla3;
use LWP::Simple;

my ($l, $p) = ('aalexandrov@swsoft.com', 'OLB4xijy');
my @ppl = grep s/^EMAIL;TYPE=WORK,PREF,INTERNET:(.+)/$1/, split "\n", get 'http://intranet/admin/staff/getPGP.php?Structure[action]=get_group_vcf&Structure[GroupID]=22&Structure[IncludeSubgroups]=yes'; 
push @ppl, 'mshumilov@swsoft.com';

my $bz = new WWW::Bugzilla3(site => 'pembugs');
$bz->login($l, $p);

my %r = map { $_ => [ 
	scalar $bz->search(reporter => $_),
	scalar $bz->search(reporter => $_, resolution => 'INVALID'),
] } @ppl;
print "$_ - " . join('/', @{$r{$_}}) . "\n" 
	for sort { $r{$b}->[0] <=> $r{$a}->[0] } keys %r;
