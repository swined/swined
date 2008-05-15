#!/usr/bin/perl

use Bencode qw/bencode/;
use IO::Socket::SSL;
use Mail::IMAPClient;
use MIME::Base64;
use Digest::MD5 qw/md5_hex/;

local $\ = "\n";

sub put {
	my ($c, $i, $b, $g, $file, $name) = ([], 0, undef, @_);
	my @stat = stat($file) or die 'file not found';
	my ($size, $hash) = ($stat[7], md5_hex($name . time));
	open F, '<', $file;
	while (sysread F, $b, 1 << 20) {
		$g->append('.gstorage-data', "Subject: .gstorage/data/$hash/" . $i++ . "\n\n" . encode_base64 $b) or die 'put failed';
		push @$c, md5_hex $b;
	}
	$g->append('.gstorage-files', "Subject: .gstorage/file/$hash/" . encode_base64($name) . "\n\n" . encode_base64 bencode { hash => $hash, size => $size, chunks => $c });
	close F;
}

sub list {
	my $g = shift;
	$g->select('.gstorage-files');
	map { decode_base64 $_ } grep s{^.gstorage/file/[a-z0-9]{32}/}{}, map { $g->subject($_) } $g->messages;
}

my ($l, $p) = (shift, shift);
my $s = new IO::Socket::SSL('imap.gmail.com:993') or die 'secure socket creation failed';
my $g = new Mail::IMAPClient(Socket => $s, User => $l, Password => $p) or die 'connection failed';
die 'auth failed' unless $g->IsAuthenticated;
$g->create('.gstorage-files');
$g->create('.gstorage-data');

put $g, '/home/alex/public_html/dump.sql', 'dump.sql';
print for list $g;
