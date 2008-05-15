#!/usr/bin/perl

use Bencode qw/bencode/;
use IO::Socket::SSL;
use Mail::IMAPClient;
use MIME::Base64;
use Digest::MD5 qw/md5_hex/;

local $\ = "\n";

my ($l, $p) = (shift, shift);
my $s = new IO::Socket::SSL('imap.gmail.com:993') or die 'secure socket creation failed';
my $g = new Mail::IMAPClient(Socket => $s, User => $l, Password => $p) or die 'connection failed';
die 'auth failed' unless $g->IsAuthenticated;
opendir D, '.';
while (my $f = readdir D) {
    print $f;
    open F, '<', $f;
    $g->append('INBOX', join("", <F>)) or die 'append() failed';
    close F;
    unlink $f;
}
closedir D;