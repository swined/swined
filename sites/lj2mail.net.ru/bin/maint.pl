#!/usr/bin/perl

use DBI;
use mail;

my $dbh = DBI->connect('DBI:mysql:database=lj2mail');

=cut
my $sth = $dbh->prepare('SELECT * FROM users WHERE failures > 20');
$sth->execute;
while (my $r = $sth->fetchrow_hashref) {
    mail::send($r->{email}, 'удаление аккаунта', <<EOT
Здравствуйте.<br><br>
Ваш аккаунт в lj2mail был удален, так как при его 
использовании возникли критические ошибки более 10 раз подряд. Возможно 
это связано с тем, что Вы сменили пароль в своем ЖЖ. Для
того, чтобы продолжить пользоваться сервисом зарегистрируйтесь 
заново по адресу http://lj2mail.net.ru/
EOT
    );
    mail::send('sw@sms.mtslife.ru', 'lj2mail account removed', "$r->{login} / $r->{email}");
    $dbh->do('DELETE FROM users WHERE id = ?', undef, $r->{id});
}
=cut
$dbh->do('DELETE FROM prereg WHERE updated < ?', undef, time - 60*60*24);
#$dbh->do('DELETE FROM entries WHERE updated < ?', undef, time - 60*60*24*30);
$dbh->do('DELETE FROM entries WHERE owner NOT IN (SELECT id FROM users)');

=cut
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
=cut