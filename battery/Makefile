default: run
clean:
	rm *.class *.jar || true
build:	clean
	javac *.java
jar:	build
	jar cmf MANIFEST.MF battery.jar *.class icons
	rm *.class || true
run:	jar
	java -jar battery.jar
