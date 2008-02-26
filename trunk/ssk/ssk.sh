#!/bin/sh

test -e ~/.ssh/id_dsa.pub || (echo "DSA key not found" && exit)
sshpass -p "$2" ssh "$1" 'cd ~ && test -x .ssh || mkdir -m700 .ssh' || exit
sshpass -p "$2" ssh "$1" 'SSK=$(mktemp); echo $SSK; cat > $SSK' < ~/.ssh/id_dsa.pub || exit
echo done