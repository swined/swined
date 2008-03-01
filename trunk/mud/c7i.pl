my $k = {
	'01000000 Left' => 'west',
	'01000000 Right' => 'east',
	'01000000 Up' => 'north',
	'01000000 Down' => 'south',
	'01100000 Down' => 'down',
	'01100000 Up' => 'up',

	'01000000 L' => 'look',
	'01000000 D' => 'drink water',
	'01000000 E' => 'eat food',
	'01000000 I' => 'inventory',

	'00100000 T' => 'trip',
	'00100000 D' => 'disarm',
	'00100000 F' => 'flee',
};

delKeyBinding('.');
my $addKey = sub {
	my ($acc, $act) = @_;
	addKeyBinding($act, "^$acc\$", sub { XM::send("$act\n") }, 1);
};
&$addKey($_, $k->{$_}) for keys %$k;
