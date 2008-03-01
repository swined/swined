#!/bin/sh

export LJHOME=/tmp/ljhome-$$
LJCODE="$PWD/ljcode.tar.gz"

die() {
    echo FAILED
    rm -rf $LJHOME
    exit 1
}

if [ -e $LJCODE ]; then
    echo code snapshot already exists
    echo remove `basename $LJCODE` to regenerate
else
    mkdir -p $LJHOME/cvs || die
    cd $LJHOME/cvs || die
    svn co http://code.sixapart.com/svn/vcv/trunk vcv || die
    svn export http://code.sixapart.com/svn/livejournal/trunk/cvs/multicvs.conf || die
    vcv/bin/vcv --conf=multicvs.conf --checkout || die
    vcv/bin/vcv --conf=multicvs.conf --init || die
    rm -rf $LJHOME/cvs || die
    cd $LJHOME || die
    tar cvzf $LJCODE . || die
    rm -rf $LJHOME || die
fi
