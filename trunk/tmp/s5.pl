#!/usr/bin/perl
 
use IO::Socket::INET; 
 
my ($port, $auth_enabled, $auth_login, $auth_pass) = (3777, 1, 'user', 'pass'); 

$SIG{CHLD} = 'IGNORE';
my $bind = new IO::Socket::INET(Listen => 10, Reuse => 1, LocalPort => $port) or die "Can't bind port $port\n"; 
 
while (my $client = $bind->accept) { 
    $client->autoflush; 
    if (fork) { $client->close } 
    else { 
	$bind->close;
	close STDIN;
	close STDOUT;
	new_client($client);
	$client->close;
	exit;
    } 
} 
 
sub new_client { 
    my ($client, $buff, $success) = $_[0]; 
    sysread($client, $buff, 1); 
    return unless 5 == ord $buff;
    sysread($client, $buff, 1); 
    return unless ord $buff == sysread($client, $buff, ord $buff);
    for (split //, $buff) {
	next if $success;
	syswrite($client, "\x05\x00") and $success++ if ord == 0 && !$auth_enabled;
	do_auth($client) and $success++ if ord == 2 && $auth_enabled;
    } 
    return syswrite($client, "\x05\xFF") unless $success;
    $t = sysread($client, $buff, 3); 
    if ((substr($buff, 0, 1) == '\x05') and (ord(substr($buff, 2, 1)) == 0)) { 
	my ($host, $port, $raw) = socks_get_host_port($client); 
	return unless $host and $port;
	syswrite($client, "\x05\x00\x00" . $raw); 
	socks_connect($client, $host, $port) if 1 == ord substr($buff, 1, 1);
    } 
} 
 
sub do_auth { 
    my ($client, $buff, $login, $pass) = $_[0]; 
    syswrite($client, "\x05\x02"); 
    sysread($client, $buff, 1); 
    return unless 1 == ord $buff;
    sysread($client, $buff, 1) and sysread($client, $$_, ord $buff) for \$login, \$pass;
    return syswrite($client, "\x05\x00") if $login eq $auth_login and $pass eq $auth_pass;
    syswrite($client, "\x05\x01");
} 
 
sub socks_get_host_port { 
    my ($client, $t, $raw_host, $host, $raw_port, $port) = $_[0]; 
    sysread($client, $t, 1); 
    if (1 == ord $t) { 
	sysread($client, $raw_host, 4); 
	$host = join '.', map { ord } split //, $raw_host;
    } elsif (3 == ord $t) { 
	sysread($client, $raw_host, 1) and sysread($client, $host, ord $raw_host); 
	$raw_host .= $host; 
    } 
    sysread($client, $raw_port, 2);
    $port = ord(substr($raw_port, 0, 1)) << 8 | ord(substr($raw_port, 1, 1)); 
    return ($host, $port, $t . $raw_host . $raw_port); 
} 
 
sub socks_connect { 
    my ($client, $host, $port, $rin, $rout, $eout, $cbuffer, $tbuffer) = @_; 
    my $target = new IO::Socket::INET("$host:$port") or return; 
    $target->autoflush; 
    while ($client or $target) { 
	$$_ and vec($rin, fileno($$_), 1) = 1 for \$client, \$target; 
	select($rout = $rin, undef, $eout = $rin, 120); 
	return unless $rout or $eout;
	sysread($client, $tbuffer, 1024) or return if $client && (vec($eout, fileno($client), 1) || vec($rout, fileno($client), 1));
	sysread($target, $cbuffer, 1024) or return if $target && (vec($eout, fileno($target), 1) || vec($rout, fileno($target), 1));
	$tbuffer = substr($tbuffer, (syswrite($target, $tbuffer) or return)) while $tbuffer;
	$cbuffer = substr($cbuffer, (syswrite($client, $cbuffer) or return)) while $cbuffer;
    } 
}