package web;

use warnings;
use strict;
use CGI qw/Vars/;
use CGI::Session;
use CGI::Carp qw/fatalsToBrowser/;
use XML::Simple;
use Attribute::Args;

my $session = new CGI::Session;

sub params :ARGS { 
	return Vars;
}

sub upload :ARGS('scalar') {
	my ($name) = @_;
	return $session->query->param($name);
}

sub redirect :ARGS('scalar') {
  my ($location) = @_;
  print $session->query->redirect($location);
  exit;
}

sub xslt :ARGS('scalar', 'HASH') {
	my ($style, $data) = @_;
        my $o = XML::Simple::XMLout($data, RootName => 'root');
        $o =~ s/[^[:graph:]\s]//g;
        print $session->header(-content_type => 'text/xml; charset=UTF-8');
        print
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" .
                "<?xml-stylesheet href=\"${style}\" type=\"text/xsl\"?>\n\n".$o;
	exit;	
}

1;
