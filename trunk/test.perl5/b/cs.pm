package cs;

use 5.008;
use strict;
use warnings;

our $VERSION = '0.01';

use B qw< OPf_KIDS OPf_STACKED OPf_WANT OPf_WANT_VOID OPf_WANT_SCALAR OPf_WANT_LIST OPf_REF OPf_MOD OPf_SPECIAL OPf_KIDS >;
use B qw< OPpTARGET_MY ppname>; 
use B qw< SVf_IOK SVf_NOK SVf_POK SVf_IVisUV >;
use B::Generate;
use B::Concise;
use B::Deparse;
# use B::Utils;
sub SVs_PADMY () { 0x00000400 }     # use B qw< SVs_PADMY >;

use strict;
use warnings;

sub OPfDEREF    () { 32|64 } # #define OPpDEREF                (32|64) /* autovivify: Want ref to something: */
sub OPfDEREF_AV () { 32 }    # #define OPpDEREF_AV             32      /*   Want ref to AV. */
sub OPfDEREF_HV () { 64 }    # #define OPpDEREF_HV             64      /*   Want ref to HV. */
sub OPfDEREF_SV () { 32|64 } # #define OPpDEREF_SV             (32|64) /*   Want ref to SV. */

sub inject {

    my %args = @_;
    my $code = delete $args{code};          # what to insert
    my $target = delete $args{target};		# where to insert it

    my $preconditions = delete $args{preconditions} || [ ];

    for(my $i = 0; $i < @_; $i += 2) { 
        $_[$i] eq 'precondition' and push @$preconditions, $_[$i+1];
    }

    delete $args{precondition};

    # specifications with which to build 

    my $line = delete $args{line};
    my $label = delete $args{label};

    %args and die "unknown arguments: " . join ', ', keys %args;

    UNIVERSAL::isa($code, 'CODE') or die;

    # Build list of conditions that must be true for the injection and list of things which cannot be true

    $line and push @$preconditions, sub {
        my $op = shift;
        $op->name eq 'nextstate' or return;
        $line and $op->line == $line or return;
        return 1;
    };

    $label and push @$preconditions, sub {
        my $op = shift;
        $op->name eq 'nextstate' or return;
        $line and $op->label eq $label or return;
        return 1;
    };

    # Look up the method we're supposed to insert into

    my $cv = do { no strict 'refs'; B::svref_2object($target); }; 
    $cv->ROOT() or die "no code in target";
    $cv->STASH()->isa('B::SPECIAL') and die; # Can't locate object method "NAME" via package "B::SPECIAL"
    $cv->ROOT()->can('first') or die "target cannot do ->ROOT->first\n"; 

    my $newcv = B::svref_2object($code);
    my $newop = $newcv->ROOT;                            # $newop points to a leavesub instruction
    $newop->name eq 'leavesub' or die;
    my $newopfirst = $newop->first->first;  $newopfirst = $newopfirst->has_sibling if $newopfirst->has_sibling and $newopfirst->name  eq 'nextstate'; # was causing coredumps when the nextstate was inserted into the wrong place
    my $newoplast = do { my $x = $newopfirst; $x = $x->has_sibling while $x->has_sibling; $x; };

    my @nonrootpad = ($cv->PADLIST->ARRAY)[0]->ARRAY;

    my $redo_reverse_indices = sub {
        my $siblings = { };
        walkoptree_slow($cv->ROOT, sub { 
            my $self = shift;       return unless $self and $$self;
            my $next = $self->next; 
            my $sibl = $self->can('sibling') ? $self->sibling : undef;
            $siblings->{$$sibl} = $self if $sibl and $$sibl;
        });
        return $siblings;
    };

    my $siblings = $redo_reverse_indices->();

    my $look_for_things_to_diddle = sub {
        my $op = shift or die;       # op object
        my $level = shift;
        my $parents = shift or die;
    
        return unless $op and $$op;
        return if $op->isa('B::NULL');

    
        return unless exists $parents->[0]; # root op isn't that interesting and we need a parent
        my $parent = $parents->[-1];
    
        my $pointcut = sub {
    
            my $prev_sibling = $siblings->{$$op}; # may be undef
            my $next_sibling = $op->sibling;      # may be undef
    
            $prev_sibling->sibling($newopfirst) if $prev_sibling and $$prev_sibling;
            $newoplast->sibling($op->sibling) if $op->sibling and ${$op->sibling};
    
    
            $parent->first($newopfirst) if $parent->can('first') and ${$parent->first} == $$op;
            $parent->last($newoplast) if $parent->can('last') and ${$parent->last} == $$op;
    
            $siblings = $redo_reverse_indices->(); # only one swath of code is injected at a time, so this isn't currently needed

            goto did_pointcut;
    
        };

        for my $i (0 .. @$preconditions-1) {
            if($preconditions->[$i]->($op)) {
                splice @$preconditions, $i, 1, ();
            }
        }
    
        if(! @$preconditions) {
            $op = $op->has_sibling if $op->has_sibling and $op->name eq 'nextstate';
            $pointcut->();
            goto did_pointcut;
        }
   
        return;
    };

    walkoptree_slow($cv->ROOT, $look_for_things_to_diddle);
   die "pointcut failed";
    did_pointcut:

    fix($cv->ROOT->first, $cv->ROOT);


    # Translate the spliced-in code's idea of lexicals to match where it's spliced in to

    my @srcpad = lexicals($newcv);
    my @destpad = lexicals($cv); 

    my %destpad = map { ( $destpad[$_] => $_ ) } grep defined $destpad[$_], 0 .. $#destpad; # build a name-to-number index


    walkoptree_slow($cv->ROOT, sub {
        my $op = shift or die;       # op object
        $op->can('targ') or return;  # B::NULL cannot
        $srcpad[$op->targ] or return;
        exists $destpad{$srcpad[$op->targ]} or die "variable ``$srcpad[$op->targ]'' doesn't exist in target context";
        $op->targ($destpad{$srcpad[$op->targ]});
    });

    return 1;
}


#
# utility methods
#

my @parents = ();

sub walkoptree_slow {
    # actually recurse the bytecode tree
    # stolen from B.pm, modified
    my $op = shift;
    my $sub = shift;
    my $level = shift;
    $level ||= 0;
    $sub->($op, $level, \@parents);
    if ($op->can('flags') and $op->flags() & OPf_KIDS) {
        push @parents, $op;
        my $kid = $op->first();
        my $next;
        next_kid:
          # was being changed right out from under us, so pre-compute
          $next = 0; $next = $kid->sibling() if $$kid;
          walkoptree_slow($kid, $sub, $level + 1);
          $kid = $next;
          goto next_kid if $kid;
        pop @parents;
    }
    if (B::class($op) eq 'PMOP' && $op->pmreplroot() && ${$op->pmreplroot()}) {
        # pattern-match operators
        push @parents, $op;
        walkoptree_slow($op->pmreplroot(), $sub, $level + 1);
        pop @parents;
    }
};

sub fix {
    my ($op, $parent) = @_;
    if($op->isa('B::NULL')) {
        #return fix($op->first, $parent);
        return $op;
    }
    # $op = denull($op);
    if($op->has_sibling) {
        $op->next(fix($op->has_sibling, $parent));
    } else {
        $op->next($parent) if $parent;
    }
    if($op->has_first) {
        return fix($op->has_first, $op);
    } else {
        return $op;
    }
}

sub B::OP::has_sibling {
    my $op = shift;
    # eval { warn 'has_sibling: ' . $op->sibling; };
    return unless $op->can('sibling') and $op->sibling and ${$op->sibling}; #  and ref $op->sibling ne 'B::NULL';
    return denull($op->sibling);
}

sub B::OP::has_first {
    my $op = shift;
    # eval { warn 'has_first: ' . $op->first; };
    return unless $op->can('first') and $op->first and ${$op->first}; #  and ref $op->first ne 'B::NULL';
    return denull($op->first);
}

sub denull {
    my $op = shift;
    if( $op->isa('B::NULL') ) {
        return denull($op->first);
    } else {
        return $op;
    }
}

sub lexicals {
    my $cv = shift;
    # map { ( $_ => $padnames[$_]->PVX) }  grep { ! $padnames[$_]->isa('B::SPECIAL') } 0 .. $#padnames;
    map { $_->isa('B::SPECIAL') ? undef : $_->PVX } ($cv->PADLIST->ARRAY)[0]->ARRAY;
}

1;
