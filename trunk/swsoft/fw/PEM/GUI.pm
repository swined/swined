package PEM::GUI;

use Carp;
use WWW::Selenium;

my $se = {
	host        => 'localhost',
	port        => 4444,
	browser     => '*chrome',
	browser_url => 'http://localhost:4444/',
	auto_stop   => 1,
};

sub new {
	my ($c, %p) = @_;
	$p{timeout} = 30 unless $p{timeout};
	$p{se} = new WWW::Selenium(%$se);
	$p{se}->start;
	bless \%p;
}

sub se { shift->{se} }

sub login {
	my ($this, %p) = @_;
	print "Logging to PEM as $p{login}:$p{password}\@$p{url}\n";
	$this->se->open($p{url});
	$this->setFrame('loginFrame');
	$this->type('//input[@name=\'user\']' => $p{login},
		'//input[@name=\'password\']' => $p{password});
	$this->clickAndWait('uelidnull_doLogin', $p{screen});
}

sub loginPCP {
	my ($this) = @_;
	croak "provider's credentials are not specified" unless $this->{provider};
	$this->login(%{$this->{provider}}, screen => 'Screen ID:1.00.00.01');
}

sub click {
	my ($this, $path, %p) = @_;
	$this->setFrame($p{frame}) if $p{frame};
	$this->clickAndWait($_) for @$path;
	$this->checkScreenId($p{screen}) if defined $p{screen};
}

sub followLinks {
	my ($this, $path, %p) = @_;
	$this->click([map { "link=$_" } @$path], %p);
}

sub navigate {
	my ($this, $path, %p) = @_;
	print "Going to " . join(' > ', @$path) . "\n";
	$this->followLinks($path, %p, frame => 'navTree');
}

sub setFrame {
	my ($this, $frame) = @_;
	$this->se->select_frame('relative=top');
	$this->se->select_frame($frame);
}

sub checkScreenId {
	my ($this, $sid) = @_;
	$this->setFrame('mainFrame');
	$wid = $this->se->get_text('screenID');
	croak "screenID doesn't match! ('$wid' != '$sid')" 
		unless $wid =~ /^$sid$/;
}

sub clickAndWait {
	my ($this, $locator, $screenID) = @_;
	$this->se->click($locator);
	$this->se->wait_for_page_to_load($this->{timeout} * 1000);
	$this->checkScreenId($screenID) if defined $screenID;
        $this;
}

sub type {
	my ($this, %p) = @_;
	$this->se->type($_, $p{$_}) for keys %p;
}

1;
