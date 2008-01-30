#!/usr/bin/perl

use Bencode qw/bencode/;
use IO::Socket::SSL;
use Mail::IMAPClient;
use MIME::Base64;
use Digest::MD5 qw/md5_hex/;

local $\ = "\n";

sub put {
    my ($c, $i, $b, $gmail, $file, $name) = ([], 0, undef, @_);
    my @stat = stat($file) or die 'file not found';
    my ($size, $hash) = (@stat[7], md5_hex($name . time));
    open F, '<', $file;
    while (sysread F, $b, 1 << 20) {
	$gmail->append('.gstorage-data', "Subject: .gstorage/data/$hash/" . $i++
	. "\n\n" . encode_base64 $b) or die 'store failed';
	push @$c, md5_hex $b;
    }
    $gmail->append('.gstorage-files', "Subject: .gstorage/file/$hash/" . encode_base64($name) 
	. "\n\n" . encode_base64 bencode { hash => $hash, size => $size, chunks => $c });
    close F;
}

sub list {
    my $gmail = shift;
    $gmail->select('.gstorage-files');
    map { decode_base64 $_ } grep s{^.gstorage/file/[a-z0-9]{32}/}{},
	map { $gmail->subject($_) } $gmail->messages;
}

my ($login, $pass) = (shift, shift);

my $g = new Mail::IMAPClient(
    Socket => new IO::Socket::SSL('imap.gmail.com:993'),
    User => $login, Password => $pass,
) or die 'connection failed';
$g->create('.gstorage-files');
$g->create('.gstorage-data');

put $g, '', '';
print for list $g;
