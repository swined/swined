default: clean xpi
xpi:
	mkdir build
	cp chrome.manifest install.js install.rdf build
	mkdir build/chrome
	zip build/chrome/nsutemp.jar content/nsutemp.js content/nsutemp.xul
	cd build && zip ../nsutemp.xpi *
clean:
	rm -vrf build nsutemp.xpi
