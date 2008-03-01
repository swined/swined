#!/usr/bin/perl

use strict;
use vars qw($VERSION %IRSSI);

use Irssi qw(settings_get_str settings_set_str active_win);
use URI::Escape;

$VERSION = '0.1.3';
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'rejoin',
    description => 'This script helps you to maintain ' .
	'a list of channels, which can be easily mass-rejoined.' .
	' Very useful in conjunction with \'cron\' script.',
    license     => 'Public Domain',
);

sub setArray($@) {
    my ($n, @a) = @_;
    settings_set_str $n, join ' ', map { uri_escape $_ } @a;
}

sub getArray($) {
    map { uri_unescape $_ } split ' ', settings_get_str(shift)
}

sub awp($) { 
    active_win->print('[%Rrejoin%N] ' . shift) 
}

sub printConf {
    my ($d, $s, $c) = @_;
    awp 'channel list:';
    awp 'the list is empty' unless getArray 'rejoin_channels';
    foreach (getArray 'rejoin_channels') {
	my ($n,$c,$p) = split ' ', $_, 3;
	awp "$n/%G$c%N";
    }
}

sub delChan {
    my ($d, $s, $c) = @_;
    awp 'must be on a channel' unless $c;
    return unless $c;
    my @chan = getArray 'rejoin_channels';
    my $rm = lc "$s->{chatnet} $c->{name} ";
    my @chan = grep { lc !~ /^$rm/ } @chan;
    setArray 'rejoin_channels', @chan;
    awp "removed $s->{chatnet}/%G$c->{name}%N" unless $d eq 'silent';
}

sub addChan {
    my ($d, $s, $c) = @_;
    awp 'must be on a channel' unless $c;
    return unless $c;
    delChan 'silent', $s, $c;
    my @chan = getArray 'rejoin_channels';
    push @chan, "$s->{chatnet} $c->{name} $c->{key}";
    setArray 'rejoin_channels', @chan;
    awp "added $s->{chatnet}/%G$c->{name}%N";
}

sub doRejoin {
    foreach my $s (Irssi::servers()) {
	foreach (getArray 'rejoin_channels') {
	    my ($n, $c, $p) = split ' ', $_;
	    next unless lc $s->{chatnet} eq lc $n;
	    $s->command("join $c $p") unless $s->channel_find($c);
	} 
    }
}

Irssi::command_bind('rejoin','doRejoin');
Irssi::command_bind('rejoin_conf','printConf');
Irssi::command_bind('rejoin_add','addChan');
Irssi::command_bind('rejoin_del','delChan');

Irssi::settings_add_str('rejoin', 'rejoin_channels', '');

1;
