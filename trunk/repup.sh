#!/bin/sh

for deb in *.deb; do
    reprepro -b /var/web/deb.swined.net.ru includedeb stable $deb && rm $deb
done