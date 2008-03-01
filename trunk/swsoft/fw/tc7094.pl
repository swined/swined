#!/usr/bin/perl

use Carp;
use PEM::GUI;
use PEM::API;

my $conf = {
	provider => {
		login => 'admin',
		password => 'setup',
		url => 'https://cp.pem.dcom/',
	},
	customer => {
		id => '1000010',
	},
	st => {
		'winpleskvps' => 8,
	},
	mn => '172.16.55.1',
};

sub exec_test {
	my ($e, $s, @p) = (undef, @_);
	local $SIG{__DIE__} = sub { $e = shift }; 
	eval { &$s(@p) }, $e;
}

sub wa109783 {
	my ($api, $id) = @_;
	my $ai = $api->getAccountInfo($id);
	return if $ai->{address}->{state};
	print "Working around bug#109783\n";
	$ai->{address}->{state} = 'WA';
	$api->setAccountInfo($id, $ai);
}

sub test_1_1 {
	my $api = new PEM::API(%$conf);	
	wa109783 $api, $conf->{customer}->{id};
	print "Subscribing customer to winpleskvps\n";
	my $sub = $api->activateSubscription(
		$conf->{customer}->{id},
		$conf->{st}->{winpleskvps});
	print "Subscription: $sub\n";
	print "Subscriptions: " . join(', ', $api->getAccountSubscriptions($conf->{customer}->{id})) . "\n";
};

sub test {
	my $api = new PEM::API(%$conf);
	print $api->addDomainxx() . "\n";
}

my $e = exec_test(\&test);
print "err: $e\n" if $e;
my $e = exec_test(\&test_1_1);
print "err: $e\n" if $e;
print "--\n";
