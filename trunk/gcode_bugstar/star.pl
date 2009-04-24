#!/usr/bin/perl

use strict;
use warnings;
use db;
use WWW::Mechanize;
use Getopt::Long;

GetOptions(
    my $o = {},
    'login=s',
    'password=s',
    'bug=i',
    'project=s',
);

sub star {
    my (%O) = @_;
    my $o = \%O;
    die '--project is required' unless $o->{project};
    die '--bug is required' unless $o->{bug};
    die '--login is required' unless $o->{login};
    die '--password is required' unless $o->{password};
    my $m = new WWW::Mechanize(autocheck => 1);
    $m->get(sprintf 'http://code.google.com/p/%s/issues/detail?id=%s', $o->{project}, $o->{bug});
    $m->follow_link(text => 'Sign in');
    $m->submit_form(fields => {
    	'Email' => $o->{login},
	'Passwd' => $o->{password},
    });
    die $& if $m->content =~ /Sorry, your account has been disabled/si;
    die 'shit happened' unless $m->content =~ m|<meta http-equiv="refresh" content="0; url=&#39;(.+?)&#39;">|si;
    (my $t = $1) =~ s/&amp;/&/g;
    $m->get($t);
    die 'already starred' if $m->content =~ /star_on/;
    die 'star not found' unless $m->content =~ /star_off/;
    die 'pagegen not found' unless $m->form_with_fields('pagegen');
    $m->get(sprintf 'http://code.google.com/p/%s/issues/setstar.do?alt=js&issueid=%s&starred=1&cd=%s', $o->{project}, $o->{bug}, $m->value('pagegen'));
    die 'not starred' unless $m->content =~ /'star': 1/si;
}

db::foreach(
    'SELECT * FROM accounts WHERE site = "google_com"',
    sub {
	print sprintf "trying %s:%s\n", $_->{login}, $_->{password};
	eval { star(project => $o->{project}, bug => $o->{bug}, login => $_->{login}, password => $_->{password}) };
	if (my $e = $@) {
	    print "# $e\n";
	    if ($e =~ /account has been disabled/) {
		db::do('DELETE FROM accounts WHERE id = ?', undef, $_->{id});
	    }
	} else {
	    print "done\n";
	}
    },
);