#!/usr/bin/perl

use strict;
use vars qw($VERSION %IRSSI);

use Irssi;
use Net::OSCAR;

$VERSION = '0.0.1';
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'ircq',
    description => 'icq module for irssi',
    license     => 'Public Domain',
);

sub swp { print "[ircq] " . shift }

sub icq_connect {
    my ($uin, $pass) = @_;
    return swp "UIN or password was not specified"
	unless $uin and $pass;    
    Irssi::command("window new hidden");
    my $w = Irssi::active_win;
    $w->print("connecting to icq#$uin");
    my $o = new Net::OSCAR;
    $o->{icq_win} = $w;
    $o->set_callback_im_in(sub {
	my ($o, $s, $m, $a) = @_;
	$o->{icq_win}->print("<$s> $m");
    });
    $o->set_callback_buddy_in(sub {
	my ($o, $s, $g, $d) = @_;
	$o->{icq_win}->print("$s is online");
    });
    $o->set_callback_buddy_out(sub {
	my ($o, $s, $g) = @_;
	$o->{icq_win}->print("$s is offline");
    });
    $o->set_callback_signon_done(sub {
	my ($o) = @_;
	$o->{icq_win}->print("connected");
    });
    $w->{icq_oscar} = $o;
    $o->signon($uin, $pass);
}

my $cmd_handlers = {
    'connect' => sub { icq_connect split ' ', shift, 2 }
};

Irssi::command_bind('icq', sub {
    my ($p, $s, $c) = @_;
    my @x = split ' ', $p, 2;
    my $h = $cmd_handlers->{@x[0]};
    return &$h(@x[1]) if $h;
    swp "[ircq] Unknown command: /icq @x[0]";
});

#Irssi::timeout_add(100, sub { 
#    map { $_->{icq_oscar} } Irssi::windows();
#}, undef);

print Irssi::create_server_handle('123', 'qwe'){qwe};

1;
