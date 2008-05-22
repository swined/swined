#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use CGI qw/:all/;
use CGI::Debug;
use WT;

my $q = WT->new->dbh->selectall_hashref('SELECT * FROM torrents WHERE owner = ? AND del = 0', 'id', undef, $ENV{REMOTE_USER});
print header(-content_type => 'text/html; charset=utf-8');
print "<?xml version='1.0' encoding='UTF-8'?>\n";
print "<?xml-stylesheet type='text/xsl' href='/webtornado/list.xsl'?>\n";
print "<torrents>\n";
foreach my $r (map { $q->{$_} } keys %$q) {
    undef $r->{torrent};
    print "<torrent ";
    print "$_='$r->{$_}' " for keys %$r;
    print "/>\n";
}
print "</torrents>\n";