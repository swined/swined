build: jar
bin:	jar
	gcj --main=Main -o lj2mail build/lj2mail.jar
jar: class
	cd build && jar cfe lj2mail.jar Main *.class && cd ..
class: clean
	test -x build || mkdir build
	javac -5 -sourcepath src -d build src/*
clean:
	test -x lj2mail && rm -rf lj2mail || true
	test -x build && rm -rf build || true
run:	build
	java -jar build/lj2mail.jar
