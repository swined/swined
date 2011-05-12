#!/usr/bin/perl

use DBI;
use CGI::Framework;

my $dbh = DBI->connect('DBI:mysql:database=lj2mail');

sub require_login {
    my $f = shift;
    $f->show_template('login') unless $f->session('user');
}

sub validate_login {
    my $f = shift;
    my $u = $dbh->selectrow_hashref('SELECT * FROM users WHERE login = ? AND hash = md5(?) ORDER BY id ASC', undef, $f->form('login'), $f->form('pass'));
    $u ? $f->session('user', $u) : $f->add_error('Username or password does not match');
}

sub pre_users {
    my ($i, $sth, $f) = (0, $dbh->prepare('SELECT users.id,login,email,failures,COUNT(*) FROM users LEFT JOIN entries ON users.id = entries.owner GROUP BY users.id ORDER BY users.id ASC'), shift);
    $f->html('title', 'users');
    require_login $f;
    my $su = $f->session('user');
    if ($f->form('_item') =~ /^drop=(\d+)$/) {
	if ($su->{is_admin} or $1 == $su->{id}) {
	    $dbh->do('DELETE FROM users WHERE id = ?', undef, $1);
	    $f->html_push('_infos', { info => "dropped user $1"});
	} else {
	    $f->add_error('Only admin or account owner can do that!');
	}
    }
    $sth->execute;
    while (my $u = $sth->fetchrow_hashref) {
	next unless $su->{is_admin} or $su->{email} eq $u->{email};
	$u->{color} = $i++ % 2 ? '#CCCCCC' : '#F0F0F0';	
	$u->{color} = '#FFCC11' if $su->{id} eq $u->{id};
	$u->{can_drop} = 'yep' if $su->{is_admin} or $u->{id} == $su->{id};
	$f->html_push('users', $u);
    }
}

sub pre_prereg {
    my ($i, $sth, $f) = (0, $dbh->prepare('SELECT * FROM prereg ORDER BY id ASC'), shift);
    $f->html('title', 'prereg');
    require_login $f;
    if ($f->form('_item') =~ /^drop=(\d+)$/) {
	if ($f->session('user')->{is_admin}) {
	    $dbh->do('DELETE FROM prereg WHERE id = ?', undef, $1);
	    $f->html_push('_infos', { info => "dropped prereg $1"});
	} else {
	    $f->add_error('Only admin can do that!');
	}
    }
    $sth->execute;
    while (my $u = $sth->fetchrow_hashref) {
	next unless $f->session('user')->{is_admin} or $f->session('user')->{email} eq $u->{email};    
	$u->{color} = $i++ % 2 ? '#CCCCCC' : '#F0F0F0';
	$f->html_push('prereg', $u);
    }
}


sub pre__pre__all {
    my ($f, $t) = @_;
    $f->session('return', $t) unless $t eq 'login';
    $f->session('user', '') if $f->form('_item') eq 'logout';
}

new CGI::Framework (
    sessions_dir => "/tmp", 
    templates_dir => "../templates", 
    initial_template => "index"
)->dispatch();
