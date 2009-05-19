#!/usr/bin/perl

use warnings;
use strict;
use XML::RPC;
use Data::Dumper;

sub call {
	my ($rpc, @param) = @_;
	my $res = $rpc->call(@param);
	die $res->{faultString} if $res->{faultString};
	return $res;
}

my $rpc = new XML::RPC('http://mailtool.nameserver3.info/cgi-bin/xmlrpc.pl');
print Dumper call($rpc, 'request_confirmation', { 
	'rpc' => 'http://localhost/fake.xml',
	'data' => {
		'link' => 'http://.+?/confirm.xxx\?username=\w+',
		'to' => 'act.confirm+cfaacf@gmail.com',
		'from' => [
		],
		'subject' => [
			'kaboodle',
		],
		'text' => [
			'kaboodle',
		],
	},
});
