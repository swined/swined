#!/usr/bin/perl

use mail;
use DBI;
use MIME::Base64::Perl;
use LWP::Simple;
use Digest::MD5;
use XML::RSS::Parser::Lite;
use HTML::Entities;
use Fcntl qw/:flock/;
use Getopt::Long;

my $o = {};
GetOptions($o, 'quiet');

open(SELF, '<', $0) and flock(SELF, LOCK_EX | LOCK_NB) or exit;

sub xprint {
	print @_ unless $o->{quiet};
	1;
}

my $dbh = DBI->connect('DBI:mysql:database=lj2mail');

while (1) {
    my $user = $dbh->selectrow_hashref('SELECT * FROM users WHERE updated < ? OR updated IS NULL ORDER BY updated ASC LIMIT 1', undef, time);
    xprint("nothing to check, waiting\n") and sleep(15*60) and next unless $user;
    xprint "checking $user->{login} (" . time . ")\n";
    my $rst = get "http://lj2rss.net.ru/friends.rss?login=$user->{login}&hash=$user->{hash}";
    xprint "parsing xml\n";
    my $rss = new XML::RSS::Parser::Lite;
    {
	local $SIG{__DIE__} = sub { 
	    exit if $o->{quiet};
	    die $rst;
	};
	$rss->parse($rst);
    };
    xprint "parsing entries\n";
    for (my $i = 0; $i < $rss->count; $i++) {
	my $e = $rss->get($i);
	if (($e->get('url') =~ /http:\/\/lj2rss.net.ru\//) and
	    ($e->get('title') eq 'error')) {
	    $user->{failures}++;
	} else {
	    $user->{failures} = 0;	
	    my $hash = Digest::MD5::md5_hex $e->get('url');
	    next if $dbh->selectrow_hashref('SELECT COUNT(*) FROM entries WHERE owner = ? AND hash = ?', undef, $user->{id}, $hash)->{'COUNT(*)'};
	    my $unsub = Digest::MD5::md5_hex $e->get('url') . rand() . time();	
	    my $title = decode_entities $e->get('title');
	    xprint "sending $title\n";
	    my $link = $e->get('url');
	    my $msg = decode_entities $e->get('description');
	    $msg =~ s/^/$&<a href='$link'>Original entry<\/a> <a href='http:\/\/lj2mail.net.ru\/u$unsub'>Unsubscribe<\/a><hr \/>/s;
	    $dbh->do('INSERT INTO entries(owner, hash, updated, unsubscribe) VALUES(?, ?, ?, ?)', undef, $user->{id}, $hash, time, $unsub);
	    my $title = encode_base64 $title;
	    ($title = "=?utf-8?B?${title}?=") =~ s/[\r\n]//g;
	    mail::send($user->{email}, $title, $msg) or next if $user->{updated};
	}
    }
    $dbh->do('UPDATE users SET updated = ? WHERE id = ?', undef, 
	time() + ($user->{failures} * 60 * 15) + $user->{delta}, $user->{id});
    $dbh->do('UPDATE users SET failures = ? WHERE id = ?', undef, $user->{failures}, $user->{id});
}
