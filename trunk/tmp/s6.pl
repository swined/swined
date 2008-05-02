#!/usr/bin/perl
 
use IO::Socket::INET; 
 
my ($port, $auth_enabled, $auth_login, $auth_pass) = (3777, 1, 'user', 'pass'); 

local $\ = "\n";
my $bind = new IO::Socket::INET(Listen => 10, Reuse => 1, LocalPort => $port) or die "Can't bind port $port\n"; 
 
my $s = [[ \$bind, undef ]];
while (1) {
	print scalar @{ $s = [ grep { $_->[0] } @$s ] };
	my $c = xsel($s) or next;
	if ($c->[1]) {
		my $t;
		sysread(${$c->[0]}, $t, 1024) or do {
			$c->[0] = undef;
			next;
		};
		syswrite(${$c->[1]}, $t);
	} else {
		my $a = ${$c->[0]}->accept;
                my $t = new_client($a) or next;
                push @$s, [ \$a, \$t ], [ \$t, \$a ];
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
    return syswrite($client, "\x05\xFF") and 0 unless $success;
    $t = sysread($client, $buff, 3); 
    return unless (substr($buff, 0, 1) == '\x05') and (ord(substr($buff, 2, 1)) == 0);
    my ($host, $port, $raw) = socks_get_host_port($client); 
    return unless $host and $port;
    syswrite($client, "\x05\x00\x00" . $raw); 
    return unless 1 == ord substr($buff, 1, 1);
    my $target = new IO::Socket::INET("$host:$port") or return; 
    $target->autoflush; 
    $target;
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
 
sub xsel {
	my ($c, $rin, $rout, $eout) = shift;
	vec($rin, fileno(${$_->[0]}), 1) = 1 for @$c;
	select($rout = $rin, undef, $eout = $rin, 1);
	[grep { vec($rout | $eout, fileno(${$_->[0]}), 1) } @$c]->[0];
}
