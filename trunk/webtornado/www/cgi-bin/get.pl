#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;

my $wt = new WT;

my $tor = $wt->selectrow_hashref('SELECT * FROM torrents WHERE id = ?', undef, $wt->cgi->param('id'));
print($wt->cgi->header(-status => 404)) and exit unless $tor->{output} and -e $tor->{output};
print $wt->cgi->header(-content_type => 'application/x-tar');

$tor->{output} =~ m{^(.*)/};
chdir $1;
open STDIN, 'tar c ' . WT::shesc($') . ' |'; #'
print while <>;
