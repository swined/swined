#!/usr/bin/perl

use warnings;
use strict;

sub get {
	my ($url) = @_;
	require LWP::Simple;
	require Encode;
	return Encode::decode('cp1251', LWP::Simple::get($url));
}

sub get_forum {
	my ($forum) = @_;
	return get sprintf 'http://velo.nsk.ru/forumup.php?forum=%s', $forum;
}

sub iterate_tag {
	my ($tag, $name, $sub) = @_;
	foreach ($tag->content_list) {
		next unless ref $_;
		&$sub($_) if $_->tag eq $name;
	}
}

sub generate_id {
        my ($link) = @_;
        $link =~ /^forumdn\.php\?forum=(\w+)&topic=(\d+)&messid=(\d+)$/ or die;
        return sprintf '%s.%s.%s@velo.nsk.ru', $1, $2, $3;
}

sub get_msg {
	my ($id) = @_;
	$id =~ /^(\w+)\.(\d+)\.(\d+)\@velo\.nsk\.ru$/ or die;
	return get sprintf 'http://velo.nsk.ru/forumdn.php?forum=%s&topic=%s&messid=%s', $1, $2, $3;
}

sub find_attr {
	my ($tag, $parent, $attr) = @_;
	my $val;
	iterate_tag $tag, $parent, sub {
		$val = $_->attr($attr);
	};
	return $val;
}

sub iterate_msg {
	my ($tag, $parent, $cb) = @_;
	iterate_tag $tag, 'ul', sub {
		iterate_tag $_, 'li', sub {
			my $href = find_attr $_, 'a', 'href';
			my $id = generate_id $href;
			&$cb($id, $parent);
			iterate_msg($_, $id, $cb);
		};
	};
}

sub fetch_forum {
	my ($forum, $cb) = @_;
	require HTML::Tree;
	my $tree = new HTML::Tree;
	$tree->parse(get_forum $forum);
	iterate_tag $tree, 'body', sub {
		iterate_msg $_, undef, $cb;
	};
} 

sub format_msg {
	my ($id, $pr, $gn) = @_;
        my $msg = get_msg $id;
	my $from = 'hzkto <hzkto@velo.nsk.ru>';
        if ($msg =~ m|<a class=fmfr href=/biker\.php\?key=(.+?)>(.+?)</a>|si) {
                $from = sprintf '%s <%s@velo.nsk.ru>', $2, $1;
        } elsif ($msg =~ m|<span class=fmfr>(.+?)</span>|si) {
                $from = sprintf '%s <anonymous@velo.nsk.ru>', $1;
        }
        $msg =~ m|<div class=fms>(.+?)</div>|si;
        my $subj = $1;
        $msg =~ m|<div class=fmm>(.+?)</div>|si;
        my $body = $1;
        require MIME::Lite;
        my $m = new MIME::Lite(
                'Type' => 'multipart/mixed',
                'From' => $from,
                'Message-ID' => sprintf('<%s>', $id),
                'Newsgroups' => $gn,
		'X-NNML-Groups' => $gn,
                'Subject' => $subj,
# path ?
# date
        );
        $m->add('In-Reply-To' => sprintf '<%s>', $pr) if $pr;
        $m->add('References' => sprintf '<%s>', $pr) if $pr;
        $m->attach(
		'Type' => 'text/html; charset="utf-8"', 
		'Data' => $body,
	);
	return $m->as_string;
}

my %sent;

sub update_forum {
	my ($gn, $fn, $nn) = @_;
	printf "updating %s\n", $gn;
	fetch_forum $fn, sub {
		my ($id, $pr) = @_;
		return if $sent{$id};
		if ($nn->ihave(sprintf '<%s>', $id)) {
			$nn->datasend(format_msg $id, $pr, $gn);
		}
		$sent{$id} = 1;
	};
}

require Net::NNTP;
my $nntp = new Net::NNTP('localhost:1119', Debug => 1);
die unless $nntp;
while (1) {
	update_forum 'velonsk.main', 'forum1', $nntp;
	update_forum 'velonsk.ishops', 'ishops', $nntp;
	update_forum 'velonsk.ourbikes', 'ourbikes', $nntp;
	update_forum 'velonsk.torg', 'torg', $nntp;
	update_forum 'velonsk.trip', 'trip', $nntp;
	update_forum 'velonsk.sport', 'sport', $nntp;
	update_forum 'velonsk.tour', 'tour', $nntp;
	update_forum 'velonsk.site', 'site', $nntp;
	update_forum 'velonsk.offtopic', 'offtopic', $nntp;
	sleep 60;
}