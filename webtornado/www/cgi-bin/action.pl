#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;

my $wt = new WT;
my $u = $ENV{REMOTE_USER};
sub cp { $wt->cgi->param(shift) }

({
    'stop' => sub {
	(cp('id') eq 'all') 
	    ? $wt->do('UPDATE torrents SET active = 0 WHERE owner = ?', undef, $u)
	    : $wt->do('UPDATE torrents SET active = 0 WHERE id = ? AND owner = ?', undef, cp('id'), $u);
    }, 
    'start' => sub {
    	(cp('id') eq 'all') 
	    ? $wt->do('UPDATE torrents SET active = 1 WHERE owner = ?', undef, $u)
	    : $wt->do('UPDATE torrents SET active = 1 WHERE id = ? AND owner = ?', undef, cp('id'), $u);	    
    },
    'delete' => sub {
	$wt->do('UPDATE torrents SET active = 0, del = 1 WHERE id = ? AND owner = ?', undef, cp('id'), $u);
    },
    'set_maxratio' => sub {
	$wt->do('UPDATE torrents SET maxratio = ? WHERE id = ? AND owner = ?', undef, cp('maxratio'), cp('id'), $u);
    },
}->{cp('action')} or sub {})->();

print $wt->cgi->header(-location => '/', -status => 302);