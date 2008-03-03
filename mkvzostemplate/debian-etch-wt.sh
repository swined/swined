#!/bin/sh

REPO='http://debian.nsu.ru/debian'

./mkvzostemplate.sh -t $PWD -r $REPO -n wt -i -s <<EOF
	echo 'Acquire::http::proxy "http://gw:8080";' > /etc/apt/apt.conf
	echo 'deb http://deb.swined.net.ru/ stable main' >> /etc/apt/sources.list
EOF
