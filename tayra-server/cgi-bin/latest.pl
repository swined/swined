#!/usr/bin/perl

use warnings;
use strict;
use Attribute::Args;
use SQL::Builder;
use config;
use web;

sub isValidType :ARGS('scalar') {
	my ($type) = @_;
	return scalar grep { $_ eq $type } config::types;
}

sub getTypes :ARGS {
	my @types = grep { isValidType $_ } split /,/, web::params->{type};
	@types = config::types unless @types;
	return @types;
}

sub getRelease :ARGS('scalar') {
	my ($type) = @_;
	my $query = SQL::Builder::select
                    ->from('releases')
                    ->where('type' => $type)
                    ->order('-major', '-minor')
                    ->limit(0, 1);
	return $query->map(config::dbh)

}

web::xslt '/latest.xsl', { release => [ map { getRelease $_ } getTypes ] };
