#!/bin/sh

./mkvzostemplate.sh -i -t $PWD -r 'http://debian.nsu.ru/debian' -d sid -n demlabs -p mc,vim,ssh,elinks,simple-cdd,subversion -s <<<EOF
mkdir /demlabs
mkdir /demlabs/svn
cd /demlabs/svn && svn co http://demlabs.ru/svn/idrs --username=sw
EOF
