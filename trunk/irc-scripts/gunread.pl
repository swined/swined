#!/usr/bin/perl

use strict;
use vars qw($VERSION %IRSSI);

use Irssi;
use Encode;
use URI::Escape;
use Irssi::TextUI;
use POSIX;
use XML::Simple;

$VERSION = '0.1.5';
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'gunread',
    description => 'Adds statusbar item indicating if there are unread messages in gmail',
    license     => 'Public Domain',
);

my $forked;
my $pipe_tag;
my %lastread;

sub getFeed {
    my ($l, $p) = (Irssi::settings_get_str('gunread_user'), Irssi::settings_get_str('gunread_pass'));
    my $xml = uri_escape scalar `curl https://mail.google.com/mail/feed/atom/@_[0] -su $l:$p | iconv -f utf8 -t koi8-r 2> /dev/null`;
    $xml =~ s/%3C/</g;
    $xml =~ s/%3E/>/g;
    $xml =~ s/%3A/:/g;
    $xml =~ s/%2F/\//g;
    $xml =~ s/%3D/=/g;
    $xml =~ s/%22/"/g; #"
    $xml =~ s/%3F/?/g;
    $xml =~ s/%20/ /g;
    $xml =~ s/%0D/ /g;
    $xml =~ s/%0A/ /g;    
    $xml =~ s/%23/#/g;    
    $xml =~ s/%40/\@/g;        
#    $xml =~ s/%/%%/g;
#    print $xml;
#    $xml =~ s/%%/%/g;    
    XMLin($xml, forcearray => 1);
}

sub getNewCnt {
    my $feed = getFeed;
    my @r = (defined $feed->{fullcount}->[0] ? $feed->{fullcount}->[0] : -1);
    push @r, "<$_->{author}->[0]->{name}->[0]> $_->{title}->[0]" for @{$feed->{entry}};
    return @r;
}

sub awp($) { 
    Irssi::active_win->print('[%Rgmail%N] ' . uri_unescape shift, MSGLEVEL_CLIENTCRAP) 
}


sub gunread { 
    my ($it, $gs) = @_;
    my $u = Irssi::settings_get_str('gunread_lastres');
    $it->default_handler($gs, '{sb gmail: {nick loading}}','') if $u == -2;
    $it->default_handler($gs, '{sb gmail: {nick error}}','') if $u == -1;
    $it->default_handler($gs, '','') if $u == 0;
    $it->default_handler($gs, '{sb gmail: {nick $0}}',$u) if $u > 0;
}

sub update {
	my ($rh, $wh);
	pipe($rh, $wh);
	return if $forked;
	my $pid = fork();
	if (!defined($pid)) {
	    Irssi::print("Can't fork() - aborting");
	    close($rh); close($wh);
	    return;
	}
  	$forked = 1;
	if ($pid > 0) {
		#parent 
		close($wh);
		Irssi::pidwait_add($pid);
		$pipe_tag = Irssi::input_add(fileno($rh), INPUT_READ, \&pipe_input, $rh);
		return;
	}
	
	my @lines;
	eval {
		#child
		@lines = getNewCnt;
		#write the reply
		print ($wh join " ", map { uri_escape $_ } @lines);
		close($wh);
	};
	POSIX::_exit(1);
}

sub pipe_input {
	$forked = 0;
	my $rh = shift;
	my $rt;
	while (my $t = <$rh>) {
	    $rt .= $t;
	}
	my @rx = map { uri_unescape $_ } split " ", $rt;
	Irssi::settings_set_str('gunread_lastres', @rx[0]);
	my $i = 0;
	my %nlr;
	foreach (@rx) {
	    if ($i) {
		awp @rx[$i] unless $lastread{@rx[$i]};
		$nlr{@rx[$i]} = 1;
	    }
	    $i++;
	}
	%lastread = %nlr;
	close($rh);
	Irssi::input_remove($pipe_tag);
	Irssi::statusbar_items_redraw('gunread');
}

Irssi::settings_add_str('gunread', 'gunread_user', '');
Irssi::settings_add_str('gunread', 'gunread_pass', '');
Irssi::settings_add_str('gunread', 'gunread_lastres', '');
Irssi::settings_set_str('gunread_lastres', -2);

Irssi::statusbar_item_register('gunread', '', 'gunread');
Irssi::command('statusbar window add gunread');
Irssi::timeout_add(10000, 'update', undef);

Irssi::statusbar_items_redraw('gunread');
update;

1;



