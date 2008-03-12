build: 
	echo "#!/usr/bin/perl" > qmpc
	ls ui/*.ui | sed 's/^/puic /' | sh >> qmpc
	cat qmpc.pl >> qmpc
	perlcc -B qmpc -o qmpc
clean:
	rm qmpc