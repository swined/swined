run:	clean build
	LD_LIBRARY_PATH=.:$$LD_LIBRARY_PATH ./main

build: build_lib
	gcc -o main -L . -l shit main.c
clean:	clean_lib
	rm -vf main

build_lib:
	gcc -shared -o libshit.so shit.c
clean_lib:
	rm -vf libshit.so
