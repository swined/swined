#!/usr/bin/perl

use Net::OSCAR qw(:standard);

my ($screenname, $password) = (shift, shift);

sub im_in {
	my($oscar, $sender, $message, $is_away) = @_;
	print "[AWAY] " if $is_away;
	print "<$sender> $message\n";
}

$oscar = Net::OSCAR->new();
$oscar->set_callback_im_in(\&im_in);
$oscar->set_callback_buddy_in(sub {
	my ($o, $s, $g, $d) = @_;
	print "$s ($g) is online\n";
});
$oscar->set_callback_buddy_out(sub {
	my ($o, $s, $g) = @_;
	print "$s ($g) is offline\n";
});

$oscar->signon($screenname, $password);
while(1) {
	$oscar->do_one_loop();
}
