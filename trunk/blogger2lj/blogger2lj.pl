#!/usr/bin/perl

use URI;
use XML::Atom::Feed;
use LWP::UserAgent;
use Digest::MD5 qw/md5_hex/;
use URI::Escape qw/uri_escape_utf8/;
use HTTP::Request;

my $c;
eval `cat ~/.blogger2lj.conf.pl`;

mkdir $c->{cache};
die 'failed to initialize cache' unless -e $c->{cache};

my $feed = new XML::Atom::Feed(new URI($c->{url}));
foreach my $entry (reverse $feed->entries) {
    my $cache = "$c->{cache}/" . md5_hex $entry->link->href;
    next if -e $cache;
    use Data::Dumper;
    my $content = $entry->content->body . "<br><br>[Crossposted from <a href='$c->{url}'>$c->{name}</a>] [<a href='" . $entry->link->href . "'>Comments</a>]";
    my @t = localtime;    
    my $data = {
	ver => 1,
	mode => postevent, 
	user => $c->{ljuser}, 
	hpassword => md5_hex($c->{ljpass}),
	event => $content,
	prop_opt_nocomments => 1,
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
