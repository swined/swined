#!/bin/sh

./mkvzostemplate.sh -t $PWD -n ljbase -s -r http://debian.nsu.ru/debian -p \
mc,tsocks,subversion,perl,apache,libapache-mod-perl,\
libclass-autouse-perl,libcompress-zlib-perl,libdatetime-perl,libdbi-perl,\
libdbd-mysql-perl,libdigest-hmac-perl,libdigest-sha1-perl,\
libhtml-tagset-perl,libhtml-parser-perl,libimage-size-perl,libio-stringy-perl,\
libmailtools-perl,libmime-lite-perl,libmime-perl,libnet-dns-perl,\
libtext-simpletable-perl,libunicode-maputf8-perl,liburi-perl,libwww-perl,\
libxml-simple-perl,libclass-accessor-perl,libclass-data-inheritable-perl,\
libclass-trigger-perl,libcrypt-dh-perl,libmath-bigint-gmp-perl,\
libproc-process-perl,librpc-xml-perl,libsoap-lite-perl,\
libstring-crc32-perl,liburi-fetch-perl,libxml-atom-perl,libxml-rss-perl << EOF
apt-get update || exit 1
apt-get install -myf --force-yes mysql-server mysql-client libgd-graph-perl libgd-gd2-perl perlmagick || exit 1
apt-get clean || exit 1
EOF
# libnet-perl
