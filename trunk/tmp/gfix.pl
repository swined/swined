#!/usr/bin/perl

use LWP::UserAgent;
use HTTP::Cookies;

my @c = grep s/[\r\n]//g, `cat conf`;
my ($l, $p, $i) = (@c[0], @c[1], 1);

my $ua = LWP::UserAgent->new(requests_redirectable => ['GET', 'HEAD', 'POST']);
$ua->cookie_jar(HTTP::Cookies->new);
$ua->post('https://www.google.com/accounts/ServiceLoginAuth', { Email => $l, Passwd => $p });

while ($i) {
    $i=0;
    my $sr = $ua->get('http://mail.google.com/mail/x/x/?ie=UTF-8&s=q&q=l%3Aunread+%21l%3Astar+%21l%3Ainbox&nvp_site_mail=%D0%9F%D0%BE%D0%B8%D1%81%D0%BA+%D0%BF%D0%BE%D1%87%D1%82%D1%8B')->content;
    my $base = $1 if $sr =~ /<base href="(.*?)">/;
    my @msg = grep s/&amp;/&/g, grep s/.*<a href="(.*?th.*?)">.*/$base\1/, split '</a>', $sr;
    foreach (@msg) {
	my $xr = $ua->get($_)->content;
	my $base = $1 if $xr =~ /<base href="(.*?)">/;
	my @xsg = grep !/ /, grep s/&amp;/&/g, grep s/.*<a href="(.*?th.*?)">.*/$base\1/, split '</a>', $xr;
	$ua->get($_) for @xsg;
	$i++;
    }
#    $i=0;
}
