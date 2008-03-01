#!/usr/bin/perl

use strict;
use vars qw($VERSION %IRSSI);

use Irssi;
use Irssi::TextUI;

$VERSION = '0.1.0';
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'loadavg',
    description => 'Adds statusbar item indicating the average cpu load',
    license     => 'Public Domain',
);

sub cat { open F, '<' . shift; my $r; while (my $t = <F>) { $r .= $t }; close F; $r }

sub sbUpdate { Irssi::statusbar_items_redraw('loadavg') }

sub loadavg { 
    my ($it, $gs) = @_;
    my $la = cat '/proc/loadavg';
    $la =~ s/[\r\n]//gs;
    $la =~ s/\s*\S*$//;
    $it->default_handler($gs, '{sb $*}', $la);
}

Irssi::statusbar_item_register('loadavg', '', 'loadavg');
Irssi::command('statusbar window add loadavg');
Irssi::timeout_add(1000, 'sbUpdate', undef);

1;
