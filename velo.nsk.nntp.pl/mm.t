#!/usr/bin/perl

sub msg {
	my ($subject, $body, $id, $parent, $group, $author, $date) = @_;
	return sprintf "Content-type: text/html\nMessage-ID: <%s>\nNewsgroups: %s\nFrom: %s\nDate: %s\nSubject: %s\nIn-Reply-To: <%s>\n\n%s\n", 
		$id, $group, $author, $date, $subject, $parent, $body;
}

die msg 'subject', 'body', 'a.b.c@velo.nsk.ru', 'd.e.f@velo.nsk.ru', 'ru.nsk.velo.main';

