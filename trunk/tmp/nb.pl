#!/usr/bin/perl

use Net::BitTorrent;

local $\ = "\n";
my $client = new Net::BitTorrent;
$client->add_session({ path => 'test.torrent' });
$client->set_callback('tracker_announce', sub { print 'tracker_announce' });
$client->set_callback('tracker_announce_okay', sub { print 'tracker_announce_okay' });
$client->set_callback('tracker_connect', sub { print 'tracker_connect' });
$client->set_callback('tracker_disconnect', sub { print 'tracker_disconnect' });
$client->set_callback('tracker_error', sub { 
    my ($c, $m) = @_;
    print sprintf 'tracker_error: %s', $m;
});
$client->set_callback('tracker_incoming_data', sub { print 'tracker_incoming_data' });
$client->set_callback('tracker_outgoing_data', sub { print 'tracker_outgoing_data' });
$client->set_callback('tracker_scrape', sub { print 'tracker_scrape' });
$client->set_callback('tracker_scrape_okay', sub { print 'tracker_scrape_okay' });
$client->do_one_loop while 1;
