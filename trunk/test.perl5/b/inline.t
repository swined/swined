#!/usr/bin/perl

use Inline C;
use B::Generate;

sub op_insert {
        my ($target, $op, @ops) = @_;
        $op->next($target->next);
        $target->next($op);
        main::op_insert($op, @ops) if @ops;
        return;
}

sub foo {
	print "foo()\n";
}

sub bar {
	print "bar()\n";
}

sub baz {
	print "baz()\n";
}

sub inject {
	my ($sub, $code) = @_;
	print "ins()\n";
	my $b = B::svref_2object($sub);
	my $gv = new B::PADOP('gv', 0);
	B::PADOP::padix($gv, _pl_push($sub, $code));
	main::op_insert($b->START, $gv, new B::UNOP('entersub', 0, 0));
	return;
}

inject \&foo, \&bar;
inject \&bar, \&baz;
inject \&foo, sub { print "anon()\n" };
foo();

__END__
__C__

int _av_push(AV *av, SV *sv) {
	SvREFCNT_inc(sv);
	av_push(av, sv);
	return av_len(av);
}

int _pl_push(CV *sub, SV *val) {
	AV *pad = CvPADLIST(sub);
	AV *keys = *av_fetch(pad, 0, 0);
	AV *vals = *av_fetch(pad, 1, 0);
	_av_push(keys, Nullsv);
	return _av_push(vals, val);
}
