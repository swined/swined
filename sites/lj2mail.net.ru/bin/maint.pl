#!/usr/bin/perl

use DBI;
use mail;

my $dbh = DBI->connect('DBI:mysql:database=lj2mail');
$dbh->do('DELETE FROM prereg WHERE updated < ?', undef, time - 60*60*24);
$dbh->do('DELETE FROM entries WHERE owner NOT IN (SELECT id FROM users)');

my $sth = $dbh->prepare('SELECT COUNT(*) FROM users');
$sth->execute;
my $uc = $sth->fetchrow_hashref->{'COUNT(*)'};
my $sth = $dbh->prepare('SELECT COUNT(*) FROM prereg');
$sth->execute;
my $pc = $sth->fetchrow_hashref->{'COUNT(*)'};
my $sth = $dbh->prepare('SELECT COUNT(*) FROM users WHERE failures > 0');
$sth->execute;
my $rc = $sth->fetchrow_hashref->{'COUNT(*)'};
my $sth = $dbh->prepare('SELECT COUNT(*) FROM entries');
$sth->execute;
my $ec = $sth->fetchrow_hashref->{'COUNT(*)'};
my $sth = $dbh->prepare('SELECT COUNT(*) FROM entries WHERE updated > ?');
$sth->execute(time - 60*60*24);
my $et = $sth->fetchrow_hashref->{'COUNT(*)'};
mail::send('sw@sms.mtslife.ru', 'lj2mail stats', "+$pc -$rc / $uc / $et / $ec");
