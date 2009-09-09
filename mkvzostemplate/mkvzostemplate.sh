#!/bin/bash

die() {
    if [ -z "$*" ]; then
	echo FAILED
    else 
	echo $*
    fi
    rm -rf $DIR
    exit 1
}

DIST='etch'
NAME=''
PKGS=''
TEMP='/tmp'

while [ ! -z "$1" ]; do
    case "$1" in
	--dist|-d) shift && DIST=$1 ;;
	--repository|-r) shift && REPO=$1 ;;
	--name|-n) shift && NAME=$1 ;;
	--pkgs|-p) shift && PKGS=$1 ;;
	--temp|-t) shift && TEMP=$1 ;;
	--script|-s) SCRIPT='yes' ;;
	--install|-i) INSTALL='yes' ;;
	--help|--usage) HELP='yes' ;;
	*) 
	    echo Unknown param: $1
	    echo See \'$0 --help\' for usage details
	    exit 1 
	;;
    esac
    shift
done

if [ ! -z "$HELP" ]; then
cat << EOF
I'm too lazy to write this help :)
EOF
    exit
fi

[[ "$DIST" =~ "[^a-z]" ]] && die Bad dist name: $DIST
[[ "$NAME" =~ "[^a-z0-9]" ]] && die Bad template name: $NAME
[[ "$PKGS" =~ "[^a-z0-9,.+-]" ]] && die Bad package list

[ ! -z "$NAME" ] && NAME="-$NAME"
[ ! -z "$PKGS" ] && PKGS="--include=$PKGS"
TPL="debian-$DIST$NAME-$(uname -m)"
DIR="$TEMP/$TPL-$$"
OUT="$PWD/$TPL.tar.gz"

echo Creating $TPL template
[ -e "$OUT" ] && echo already created, skipping && exit 0

/usr/sbin/debootstrap $PKGS $DIST $DIR $REPO || die
LC_ALL=C chroot $DIR << EOF || die
chmod 700 /root || exit 1
usermod -L root || exit 1
sed -i -e '/getty/d' /etc/inittab || exit 1
sed -i -e 's@\\([[:space:]]\\)\\(/var/log/\\)@\\1-\\2@' /etc/syslog.conf || exit 1
rm -f /etc/mtab || exit 1
ln -s /proc/mounts /etc/mtab || exit 1
update-rc.d -f klogd remove || exit 1
update-rc.d -f quotarpc remove || exit 1
update-rc.d -f exim4 remove || exit 1
update-rc.d -f inetd remove || exit 1
apt-get clean || exit 1
EOF
[ ! -z "$SCRIPT" ] && (cat | chroot $DIR || die)
cd $DIR && tar -vzcf $OUT . || die
rm -rf $DIR || die
[ ! -z "$INSTALL" ] && (mv $OUT /vz/template/cache/ || die )
