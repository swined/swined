#!/bin/sh

REPO='http://debian.nsu.ru/debian'

./mkvzostemplate.sh -t $PWD -r $REPO -n usr -p mc,tsocks,subversion
