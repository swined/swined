package mail;

use Mail::Sender;

sub send {
    Mail::Sender->new({
	smtp => 'localhost',
	from => 'Lj2Mail <null@lj2mail.net.ru>',
    })->MailMsg({
	to => shift,
	subject => shift,
	encoding => 'base64',	
	charset => 'utf8',
	ctype => 'text/html',
	msg => shift,
    });
}

1
