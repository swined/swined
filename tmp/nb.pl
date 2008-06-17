#!/usr/bin/perl

use IO::String;
use Net::BitTorrent;

sub cat {
    open my $f, '<', shift;
    my $r = join '', <$f>;
    close $f;
    return $r;
}

my $meta = IO::String(cat 'test.torrent');