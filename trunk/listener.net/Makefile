build: clean
	gmcs2 main.cs
clean:
	rm -f main.exe
run: build
	mono main.exe
trace: build
	mono --trace=program main.exe
ci: clean
	svn ci -m' '
