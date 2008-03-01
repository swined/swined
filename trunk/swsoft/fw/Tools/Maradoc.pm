package Tools::Maradoc;

use Carp;
use URI::Escape;
use LWP::UserAgent;

sub new {
	my ($c, %p) = @_;
	$p{ua} = new LWP::UserAgent;
	$p{ua}->cookie_jar({});
#	$p{ua}->requests_redirectable(['GET', 'POST']);
	$p{url} = 'http://qa-doc-creation.pem.plesk.ru/';
	bless \%p;
}

sub post {
	my ($s, $u, %d) = @_;
	my $rq = new HTTP::Request(POST => $u);
	$rq->content_type('application/x-www-form-urlencoded');
	$rq->content(join '&', map { "$_=" . uri_escape($d{$_}) } keys %d);
	$s->{ua}->request($rq)->content;
}

sub login {
	my $s = shift;
	print $s->post("$s->{url}/admin.php",
		user_name => $s->{user},
		user_password => $s->{pass},
		object => 'user',
		action => 'Login',
	);
	$_ =~ /Login and stop hacking!/ ? 
		croak "logon to maradoc failed" : $s;
}

sub assignedTestCases {
	my ($s) = @_;
	$s->{ua}->get("$s{url}engineer.php")->content;
}

1
