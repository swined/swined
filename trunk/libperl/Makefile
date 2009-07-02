default: clean build run
build:
	cc -o p p.c `perl -MExtUtils::Embed -e ccopts -e ldopts`
clean:
	rm -vf p
run:
	./p
