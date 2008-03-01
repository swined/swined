use Encode;

my $k = {
	'01000000 Left' => 'west',
	'01000000 Right' => 'east',
	'01000000 Up' => 'north',
	'01000000 Down' => 'south',
	'01100000 Down' => 'down',
	'01100000 Up' => 'up',

	'01000000 L' => 'look',
	'01000000 K' => 'оглядеться',
	'01000000 D' => 'пить мех',
	'01000000 E' => 'есть хлеб',
	'01000000 I' => 'inventory',
	'01000000 R' => 'зачитать возврат',
	'01000000 H' => 'вскочить',
	'01100000 H' => 'соскочить',
	'01000000 W' => 'stand',
	'01000000 M' => 'взять все все.труп',
	
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

my $grp = 'Михлай';

delTrigger('.');
addTrigger('All', '.', sub {
	print local $_ = shift;
	s/.\[\d+;\d+m//g, s/[\r\n]//g;
	$_ = encode_utf8 $_;

	XM::send("вскочить\n") if /Михлай вскочил на коня/;
	XM::send("соскочить\n") if /Михлай соскочил с коня/;	
}, 1);
