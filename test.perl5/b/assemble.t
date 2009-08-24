#!/usr/bin/perl

use warnings;
use strict;
use B::Assembler;

sub printsub {
	die;
}

B::Assembler::newasm(\&printsub);
B::Assembler::assemble('OP code');
B::Assembler::endasm();
