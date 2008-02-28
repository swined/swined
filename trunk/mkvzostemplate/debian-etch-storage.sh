#!/bin/sh

REPO='http://debian.nsu.ru/debian'

./mkvzostemplate.sh -t $PWD -r $REPO -n storage -p proftpd -s << EOF
addgroup ftpadmin
mkdir -m 775 /home/ftp/upload
mkdir -m 775 /home/ftp/pub
chown -R ftp:ftpadmin /home/ftp
cat >> /etc/proftpd/proftpd.conf << xEOF
<Anonymous ~ftp>
User                            ftp
Group                           ftpadmin
UserAlias                       anonymous ftp
RequireValidShell               off
<Directory *>
	<Limit WRITE>
		DenyAll
	</Limit>
</Directory>
<Directory upload/*>
	Umask                           022  022
	<Limit STOR DELE READ WRITE>
		AllowAll
	</Limit>
</Directory>
</Anonymous>
xEOF
EOF
