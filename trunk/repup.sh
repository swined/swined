#!/bin/sh

REP=/var/web/deb.swined.net.ru
for deb in *.deb; do
    reprepro -b $REP includedeb stable $deb && rm $deb
done
