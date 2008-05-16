#!/usr/bin/perl

=cut

use URI;
use LJ::Simple;
use Net::Netrc;
use XML::Atom::Feed;
use Digest::MD5 qw/md5_hex/;

$LJ::Simple::UTF = 0;
my $c;
eval `cat ~/.blogger2lj.conf.pl`;
mkdir $c->{cache};
die 'failed to initialize cache' unless -d $c->{cache};
my $lj = Net::Netrc->lookup('livejournal.com') || die "~/.netrc has no lj credentials\n";
my $feed = new XML::Atom::Feed(new URI($c->{url}));
foreach my $e (reverse $feed->entries) {
    my $cache = "$c->{cache}/" . md5_hex $e->link->href;
    next if -e $cache;
    my $content = $e->content->body . "<br><br>[Crossposted from <a href='$c->{url}'>$c->{name}</a>] [<a href='" . $e->link->href . "'>Comments</a>]";
    LJ::Simple::QuickPost(user => $lj->login, pass => $lj->password, entry => $content, html => 1) || die "failed to post to LJ\n";
    open(F, '>', $cache) and close(F);
}

=cut

use URI;
use XML::Atom::Feed;
use LWP::UserAgent;
use Digest::MD5 qw/md5_hex/;
use URI::Escape qw/uri_escape_utf8/;
use HTTP::Request;
use Net::Netrc;


my $c;
eval `cat ~/.blogger2lj.conf.pl`;
mkdir $c->{cache};
die 'failed to initialize cache' unless -d $c->{cache};
my $lj = Net::Netrc->lookup('livejournal.com') || die "LJ credentials not found\n";
my $feed = new XML::Atom::Feed(new URI($c->{url}));
foreach my $entry (reverse $feed->entries) {
    my $cache = "$c->{cache}/" . md5_hex $entry->link->href;
    next if -e $cache;
    my $content = $entry->content->body . "<br><br>[Crossposted from <a href='$c->{url}'>$c->{name}</a>] [<a href='" . $entry->link->href . "'>Comments</a>]";
    my @t = localtime;    
    my $data = {
	ver => 1,
	mode => postevent, 
	user => $lj->login, 
	hpassword => md5_hex($lj->password),
	event => $content,
#	prop_opt_nocomments => 1,
	year => $t[5] + 1900,
	mon => $t[4] + 1,
	day => $t[3],
	hour => $t[2],
	min => $t[1],
    };
    my $req = new HTTP::Request(POST => 'http://www.livejournal.com/interface/flat');
    $req->header('Content-type' => 'application/x-www-form-urlencoded');
    $req->content(join("&", map { "$_=" . uri_escape_utf8($data->{$_}) } keys %$data));
    die $_ if local $_ = { split /\n/, LWP::UserAgent->new->request($req)->content }->{errmsg};
    open(F, '>', $cache) and close(F);
}

1