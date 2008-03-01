#!/usr/bin/perl

use strict;
use vars qw($VERSION %IRSSI);

use Irssi qw(settings_get_str settings_set_str active_win);
use URI::Escape;

$VERSION = '0.1.1';
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'spark',
    description => 'Some usefull tools for _spark bot',
    license     => 'Public Domain',
);

sub spBan {
    my ($p,$s,$c) = @_;
    my ($n,$t,$r) = split ' ', $p, 3;
    my $N = $c->nick_find($n);
    $c->print('specified nick is not on the channel'), return unless $N;
#    (my $h = $N->{host}) =~ s/^.*@/*!*@/;
    (my $h = $N->{host}) =~ s/^/*!/;
    $c->print('host detection failed'), return unless $h;
    $c->command("say !ban $h $t $r");
}

sub sp1440 {
    my ($p,$s,$c) = @_;
    my ($n,$r) = split ' ', $p, 2;
    spBan "$n 1440 $r", $s, $c;
}

sub spAnn {
    my ($p,$s,$c) = @_;
    spBan "$p 1440 annoying behavior", $s, $c;
}

sub spFlood {
    my ($p,$s,$c) = @_;
    spBan "$p 1440 flood", $s, $c;
}

Irssi::command_bind('spark ban','spBan');
Irssi::command_bind('spark 1440','sp1440');
Irssi::command_bind('spark ann','spAnn');
Irssi::command_bind('spark flood','spFlood');
Irssi::command_bind('spark' => sub {
    my ( $data, $server, $item ) = @_;
    $data =~ s/\s+$//g;
    Irssi::command_runsub ( 'spark', $data, $server, $item ) ;
});

1;
