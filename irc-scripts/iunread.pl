#!/usr/bin/perl

use strict;
use vars qw($VERSION %IRSSI);

use Irssi;
use Irssi::TextUI;

$VERSION = '0.1.2';
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'iunread',
    description => 'Adds statusbar item indicating if there are unread messages in centericq',
    license     => 'Public Domain',
);

my (%contacts, %ihatime, %iratime, %irstatus);

sub cat { open F, '<' . shift; my $r; while (my $t = <F>) { $r .= $t }; close F; $r }

sub getLastHist {
    my ($d, $i) = @_;
    my $t = cat "$d/$i/history";
    $t =~ /\x0C\n(IN|OUT)\n(MSG|AUTH)\n(\d*)\n\d*\n[^\x0C]*?$/s ? $3 : 0;
}

sub getLastRead {
    my ($d, $i) = @_;
    (my $t = cat "$d/$i/lastread") =~ s/\D//g;
    $t or 0;
}

sub isUnread {
    my ($d, $i) = @_;
    my @hr = stat "$d/$i/history";
    my @rr = stat "$d/$i/lastread";
    return $irstatus{$i} if @hr[9] eq $ihatime{$i} and @rr[9] eq $iratime{$i};
    ($ihatime{$i}, $iratime{$i}) = (@hr[9], @rr[9]);
    $irstatus{$i} = getLastRead($d, $i) < getLastHist($d, $i)
}

sub getNick {
    my ($d, $i) = @_;
    my @info = split "\n", cat "$d/$i/info";
    return $i if @info[45] !~ /\w/;
    return @info[45] or $i;
}

sub getContacts { # %contacts
    my ($d) = (@_);
    opendir ICQDIR, $d;
    while (my $f = readdir ICQDIR) {
	$contacts{$f} = getNick $d, $f if $f =~ /^\d*$/;
    }
    closedir ICQDIR;
}

sub dir { $ENV{HOME} . '/.centericq' }

sub sbUpdate {
    Irssi::statusbar_items_redraw('iunread');
}

sub iunread { # %contacts
    my ($it, $gs) = @_;
    my @ur = map { $contacts{$_} } grep { isUnread dir, $_ } keys %contacts;
    if (@ur) {
	my @fm = map { "{sb icq: $_}" } join ', ', map { 
	    $_ =~ s/\$/%\$/g; 	
	    $_ =~ s/}/%}/g; 
	    $_ =~ s/{/%{/g; 
	    "{nick $_}";
	} @ur;
	$it->default_handler($gs, @fm[0], '');
    } else {
	$it->default_handler($gs, '','');
    }
}

sub upCont {
    getContacts dir;
}

upCont;

Irssi::statusbar_item_register('iunread', '', 'iunread');
Irssi::command('statusbar window add iunread');
Irssi::timeout_add(1000, 'sbUpdate', undef);
Irssi::timeout_add(60000, 'upCont', undef);

1;
