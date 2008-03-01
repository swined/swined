#!/usr/bin/perl

use DBI;
use CGI qw/param/;
use Mail::Sender;
use Digest::MD5;

my $dbh = DBI->connect('DBI:mysql:database=lj2mail');

sub sqw { 
    my $sth = shift->prepare(shift);
    $sth->execute;
    my $h = $sth->fetchrow_hashref;
    $sth->finish;
    return $h;
}

my $data = {
    action => param('action'),
    login => lc(param('login')),
    hash => lc(Digest::MD5::md5_hex(param('pass'))),
    vhash => param('hash'),
};

my $email = param('addr');
my $vrf = lc(Digest::MD5::md5_hex(rand() . time()));

if ('prereg' eq $data->{action}) {
    $dbh->do("INSERT INTO prereg(login, hash, email, verify, updated) VALUES(?, ?, ?, ?, ?)",
	undef, $data->{login}, $data->{hash}, $email, $vrf, time);
    my $mailer = new Mail::Sender({
	smtp => 'localhost',
	from => 'Lj2Mail <null@lj2mail.net.ru>',
	encoding => 'base64',	
	charset => 'utf8',
    });
    $mailer->MailMsg({
	to => $email,
	subject => 'активация аккаунта',
	ctype => 'text/plain',
	msg => <<EOT
Здравствуйте.

Вы получили это письмо, так как Ваш e-mail был использован при регистрации на сайте lj2mail.net.ru
Если Вы не регистрировались на указанном сайте, просто проигнорируйте и удалите это письмо.

Для продолжения регистрации пройдите по следующей ссылке: 
http://lj2mail.net.ru/$vrf
EOT
    });
    $data->{info} = "Вам было отправлено письмо для подтверждения регистрации";
} elsif ('regvrf' eq $data->{action}) {
    my $hash = $dbh->quote($data->{vhash}, 'string');
    my $pr = sqw $dbh, "SELECT * FROM prereg WHERE verify = $hash";
    if ($pr) {
	$dbh->do("INSERT INTO users(login, hash, email) VALUES(?, ?, ?)",
	    undef, $pr->{login}, $pr->{hash}, $pr->{email});
	$dbh->do("DELETE FROM prereg WHERE verify = ?", undef, $data->{vhash});
	open F, '|mail sw@sms.mtslife.ru';
	print F "lj2mail\nnew user:\n$pr->{login} / $pr->{email}";
	close F;
	$data->{info} = "Спасибо за регистрацию";
    } else {
	$data->{error} = "Ссылка недействительна. Попобуйте зарегистрироваться еще раз.";
    }
} elsif ('unsub' eq param 'action') {
    my $hash = $dbh->quote($data->{vhash}, 'string');
    my $uid = sqw($dbh, "SELECT * FROM entries WHERE unsubscribe = $hash")->{owner};
    if ($uid) {
        my $pr = sqw $dbh, "SELECT * FROM users WHERE id = $uid";
	$dbh->do('DELETE FROM users WHERE id = ?', undef, $uid);
	$data->{info} = "Вы успешно отписались от lj2mail";
	open F, '|mail sw@sms.mtslife.ru';
	print F "lj2mail unsub user:\n$pr->{login} / $pr->{email}";
	close F;	
    } else {
	$data->{error} = "Неверная ссылка";
    }
} else { }

my ($ed, $id) = (($data->{error} ? 'block' : 'none'), 
    ($data->{info} ? 'block' : 'none'));

print <<EOF
Content-type: text/html; charset=utf8

<html>
    <head>
	<title>lj2mail</title>
	<link rel="stylesheet" href="style.css" />
    </head>
    <body>
	<center>
	    <div class="error" style="display: $ed"><b>$data->{error}</b></div>	
	    <div class="info" style="display: $id">$data->{info}</div>
	    <div class="main">
		<div class="form">
		    <form method="post">
			<input type="hidden" name="action" value="prereg" />
			Email
			<input type="text" name="addr" class="usermail" />
			Login
			<input type="text" name="login" class="username" />
			Password
			<input type="password" name="pass" class="userpass" />
			<input type="submit" class="btsubmit" value="Register" />
		    </form>
		</div>
		<p><b>Что это такое?</b><br>lj2mail - сервис автоматически 
		отправляющий по почте новые посты в Вашей френдленте</p>
		<p><b>Зачем это надо?</b><br>чтобы не тратить время и траффик 
		на обновление френдленты - новые посты сами придут к Вам по 
		почте</p>
	    </div>
	</center>
    </body>
</html>
EOF
