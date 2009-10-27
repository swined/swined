#!/usr/bin/perl

use warnings;
use strict;
use CGI;
use Cache::Memcached;
use LWP::Simple;

my $cache = new Cache::Memcached(
#    servers => ['127.0.0.1:11211'],
#    debug => 1,
) || die 'failed to connect to memcached';

sub load {
    my ($url) = @_;
    return { title => 'title', content => LWP::Simple::get($url) };
}

sub cached_get {
    my ($url) = @_;
    if (my $res = $cache->get($url)) {
	return $res;
    } else {
	my $res = load $url;
	$cache->set($url, $res) || die 'failed to add to cache';
	return $res;
    }
}

print cached_get('http://ya.ru/')->{content};
