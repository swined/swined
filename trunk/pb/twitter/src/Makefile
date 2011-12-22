LIBS=-lm -lssl -lcrypto -lcurl

build:
	gcc *.c oauth/src/*.c -o twitter $(LIBS) -DHAVE_CURL -DVERSION='"alpha"'
