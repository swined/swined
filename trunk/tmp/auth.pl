#!/usr/bin/perl

use CGI qw/param/;
use URI::Escape;
use Cache::File;
use Digest::MD5 qw/md5_hex/;
use LWPx::ParanoidAgent;
use Net::OpenID::Consumer;

my $cgi = new CGI;
my $user = param('user');
my $goto = param('goto') || $ENV{HTTP_REFERER};

sub redir { print("Location: " . shift() . "\n\n") and exit }
sub xdie { print($cgi->header, shift) and exit }

my $oid = new Net::OpenID::Consumer(
    args => $cgi,
    cache => Cache::File->new(cache_root => '/tmp'),
    consumer_secret => md5_hex($user),
);

if (my $url = $oid->user_setup_url) {
    redir $url;
} elsif ($oid->user_cancel) {
    redir $goto;
} elsif (my $vident = $oid->verified_identity) {
    # set cookie
    redir $goto;
} else {
    my $cid = $oid->claimed_identity(
	'http://www.livejournal.com/~' . uri_escape($user)
    );
    redir $goto unless defined $cid;
    my $url = $cid->check_url(
	return_to => 'http://lj2mail.net.ru/cgi-bin/auth.pl?user=' 
	    . uri_escape($user) . '&goto=' . uri_escape($goto),
	trust_root => 'http://lj2mail.net.ru/',
    );
    redir $url if $url;
    redir $goto;
}
