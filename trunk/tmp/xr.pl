#!/usr/bin/perl

use xr;

my $api = new xr(url => 'http://bugzilla.mozilla.org/xmlrpc.cgi');
#$api->useragent->requests_redirectable(['GET', 'POST']);
print keys %{$api->version};