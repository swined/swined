build:
	gmcs src/*.cs -out:lj2mail.exe -r:Mono.Data.SqliteClient -r:System.Data -r:System.Web
	gzip -c9 lj2mail.8 > lj2mail.8.gz
binary: 
	binary-arch
	binary-indep 
binary-arch: build
binary-indep: build
clean:
	rm -rf lj2mail.exe
	rm -rf lj2mail.8.gz
	rm -rf debian/files
install:
	install -m755 lj2mail.exe $(DESTDIR)/usr/lib/lj2mail/lj2mail.exe
	install -m644 lj2mail.conf.sample $(DESTDIR)/usr/share/lj2mail/lj2mail.conf.sample
	install -m755 lj2mail $(DESTDIR)/usr/bin/lj2mail
	install -m644 lj2mail.8.gz $(DESTDIR)/usr/share/man/man8
dpkg:
	#svn up
	#DEBEMAIL="Alexey Alexandrov <swined@gmail.com>" dch --increment
	#svn commit -m 'auto commit'
	svn-buildpackage -rfakeroot --svn-lintian --svn-linda -S
	#-us -uc
