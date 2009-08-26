#!/usr/bin/perl

use Inline C;

sub foo {
	my ($f, $g) = @_;
	print "foo($f, $g)\n";
}

sub bar {
	print "bar()\n";
}

_inject(\&foo, sub {
	my ($x, $y) = @_;
        print "injected($x, $y)\n";
        print "!!111oneoneone\n";
	bar();
        bar();
});
foo 29, 42;

sub test {
	my ($sub, $val) = @_;
	_inject($sub, sub {
		my ($p, $v) = @_;
		if ($p % 2) {
			print "even\n";
		} else {
			print "odd\n";
		}
		printf "inj(%s)\n", $p + $v + $val;
	});
}

#use bdump;
#bdump::sub_dump(\&test);
test(\&foo, -40);
foo 29, 42;

__END__
__C__

void _av_copy_elem(AV *src, AV *dst, int ix) {

	SvREFCNT_inc(AvARRAY(src)[ix]);
	av_push(dst, AvARRAY(src)[ix]);

}

int _append_padlist(CV *t, CV *c) {

	AV *keys_t = AvARRAY(CvPADLIST(t))[0];
	AV *vals_t = AvARRAY(CvPADLIST(t))[1];

        if (av_len(keys_t) != av_len(vals_t))
                return -1;

        AV *keys_c = AvARRAY(CvPADLIST(c))[0];
        AV *vals_c = AvARRAY(CvPADLIST(c))[1];

	if (av_len(keys_c) != av_len(vals_c))
		return -1;

	int count = av_len(keys_t) + 1;

	int i;

	for (i = 0; i <= av_len(keys_c); i++)	
		_av_copy_elem(keys_c, keys_t, i);

 	for (i = 0; i <= av_len(vals_c); i++)
                _av_copy_elem(vals_c, vals_t, i);

	return count;

}

void _shift_padix(OP *op, int padshift) {

	if (!op)
		return;

	op->op_targ += padshift;

	if (op->op_type == 7)
		((PADOP*)op)->op_padix += padshift;

	_shift_padix(op->op_next, padshift);

}

void _fix_lastop(OP *op, OP *t) {

	while (op->op_next) {

		if (!op->op_next->op_next) {
			op->op_next = t;
			return;
		}

		op = op->op_next;

	}

}

void _inject(CV *t, CV *c) {

	_shift_padix(CvSTART(c), _append_padlist(t, c));
	_fix_lastop(CvSTART(c), CvSTART(t)->op_next);	
	CvSTART(t)->op_next = CvSTART(c);

}
