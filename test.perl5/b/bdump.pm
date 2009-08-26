package bdump;

use B::Generate;

sub op_dump {
        my ($op, $cv, $i) = @_;
        printf "%s %s/%s(%s)\n", ('-' x $i), B::class($op), $op->name, $op->type;
        if ($op->isa('B::SVOP')) {
        #       printf "sv=%s\n", $op->sv->sv;
        } elsif ($op->isa('B::PADOP')) {
                printf "padix=%s\n", $op->padix;
		printf "padval=%s\n", $op->padval($cv);
        }
        op_dump($op->next, $cv, $i) unless $op->next->isa('B::NULL');
}

sub B::PADOP::padval {
        my ($self, $cv) = @_;
        my ($names, $values) = $cv->PADLIST->ARRAY;
        my @v = $values->ARRAY;
        my $r = $v[$self->padix];
	return 'null' if $r->isa('B::SPECIAL');
        return $r->sv;
}

sub cv_dump {
        my ($cv) = @_;
        print "+++++++++++++++++\n";
        op_dump($cv->START, $cv, 1);
        print "-----------------\n";
}

sub sub_dump {
	my ($sub) = @_;
	cv_dump(B::svref_2object($sub));
}

1;
