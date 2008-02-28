#!/bin/sh

die() {
	if [ -z "$*" ]; then
		echo FAILED
	else
		echo $*
	fi
	rm -rf $DIR
	exit 1
}

./debian-etch-ljbase.sh || die
./ljcode.sh || die
DIR="$PWD/debian-etch-lj-`uname -m`-$$"
OUT="$PWD/debian-etch-lj-`uname -m`.tar.gz"
echo Creating debian-etch-lj-`uname -m` template
[ -e "$OUT" ] && echo already created, skipping && exit 0
mkdir $DIR || die
tar vzxfC debian-etch-ljbase-`uname -m`.tar.gz $DIR || die
mkdir $DIR/home/lj || die
tar vzxfC ljcode.tar.gz $DIR/home/lj || die
cd $DIR && tar vzcf $OUT . || die
rm -rf $DIR
