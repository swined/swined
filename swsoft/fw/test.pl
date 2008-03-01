#!/usr/bin/perl

use PEM::API;
use RPC::XML;

my $api = new PEM::API(mn => '172.16.53.1');
print $api->pem_addDomain('xxx.bcom', 2, 1000078)->{domain_id} . "\n";
