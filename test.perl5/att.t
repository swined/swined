#!/usr/bin/perl

use MIME::Lite;                                                    

my $m = new MIME::Lite(                                                
	'Type' => 'multipart/mixed',                                   
	'From' => 'swined@gmail.com',
        'Subject' => 'subj',
);

$m->attach(
	'Type' => 'text/html; charset="utf-8"',
        'Data' => '<img src="cid:img">',
);

my $a = $m->attach(
	Type => 'image/gif',
        Path => 'image.gif',
        Filename => 'image.gif',
);
$a->attr('Content-ID' => '<img>');

print $m->as_string;

