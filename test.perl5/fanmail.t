#!/usr/bin/perl

use warnings;
use strict;
use WWW::Mechanize;

my $m = new WWW::Mechanize();
$m->get('http://www.fanmail.com/cgi/fansign.5.0.cgi');
my @domains = $m->current_form->find_input('domain')->possible_values;
my $domain = $domains[int rand scalar @domains];
$m->submit_form(fields => {
	domain => $domain,
});
$m->submit_form(fields => {
	firstname => 'vasiliy',
	lastname => 'pupkin',
});
$m->submit_form(fields => {
	name => 'vasiliy.pupkin',
});
$m->submit_form(fields => {
	forward => 'swined+vasiliy.pupkin@gmail.com',
	password1 => '123qwe',
	password2 => '123qwe',
});
$m->submit_form(
	button => 'I Accept', 
	x => 10, 
	y => 10,
);
$m->submit_form(fields => {
	bill_address => 'carmageddon ave 666',
	bill_city => 'urupinsk',
	bill_state => 'MX',
	bill_zip => '90210',
	bill_country => 'afghanistan',
	phone_num => '+1-800-555-12-56',
});
die 'account creation failed' unless $m->content =~ /Your account has been created/;
printf "%s\@%s\n", 'vasiliy.pupkin', lc $domain;
