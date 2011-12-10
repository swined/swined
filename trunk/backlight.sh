#!/bin/bash

DL=20
LP=/sys/class/backlight/pwm-backlight/brightness
BL=$(cat $LP)
case $1 in
inc) BL=$(echo "$BL+$DL" | bc) ;;
dec) BL=$(echo "$BL-$DL" | bc) ;;
**)
	echo lolwut?!
	exit 1 
esac
echo $BL > $LP
