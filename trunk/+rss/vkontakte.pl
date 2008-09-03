#!/usr/bin/perl

use Encode;
use CGI qw/:all/;
use WWW::Mechanize;
use XML::Writer;

print header('text/xml; charset=UTF-8');
my $m = new WWW::Mechanize(autocheck => 1);
$m->get('http://vkontakte.ru/login.php');
$m->submit_form(fields => { 'email' => param('email'), 'pass' => param('pass') });
$m->get('http://vkontakte.ru/news.php');
my $news = $m->content;
my $xml = new XML::Writer(OUTPUT => STDOUT);
$xml->startTag('rss', version => '2.0');
$xml->startTag('channel');
$xml->dataElement($_, "VKontakte") for ('title', 'description');
$xml->dataElement('link', "http://vkontakte.ru/");
while ($news =~ m|<td class="feedStory">(.+?)</td>|gs) {
    $xml->startTag('item');
    $xml->dataElement('guid', 'http://vkontakte.ru/', isPermaLink => 'true');
    $xml->dataElement('link', 'http://vkontakte.ru/');
    $xml->dataElement('title', 'VKontakte');
    $xml->dataElement('description', decode('cp1251', $1));
    $xml->endTag;
}
$xml->endTag;
$xml->endTag;
$xml->end;