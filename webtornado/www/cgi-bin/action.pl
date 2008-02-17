#!/usr/bin/perl

use lib '/usr/share/webtornado/pm';
use WT;

my $wt = new WT;
my $u = $ENV{REMOTE_USER};
sub cp { $wt->cgi->param(shift) }

=cut
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
=cut

use DBIx::Perlish;
DBIx::Perlish::init($wt->dbh);

({
    'stop' => sub {
	db_update { 
	    torrents->owner == $u; 
	    cp('id') eq 'all' or torrents->id == cp('id');
	    torrents->active = 0;
	};
#	(cp('id') eq 'all') 
#	    ? $wt->do('UPDATE torrents SET active = 0 WHERE owner = ?', undef, $u)
#	    : $wt->do('UPDATE torrents SET active = 0 WHERE id = ? AND owner = ?', undef, cp('id'), $u);
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