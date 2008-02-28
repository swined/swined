#!/usr/bin/perl

use Getopt::Long;

GetOptions(my $o = {
	dist => 'etch',
	arch => 'i386',
}, 'dist=s', 'arch=s', 'repo=s', 'name=s', 'pkgs=s', 'script');

die "Bad dist name!\n" if $o->{dist} =~ /\W/;
die "Bad arch name!\n" if $o->{arch} =~ /\W/;
die "Bad package list!\n" if $o->{arch} =~ /[\W,.+-]/;
die "Bad template name!\n" if $o->{name} =~ /\W/;
#die "Source repository was not specified!\n" unless $o->{repo};

$o->{name} = '-' . $o->{name} if $o->{name};
$o->{pkgs} = '--include=' . $o->{pkgs} if $o->{pkgs};
my $tpl = "debian-$o->{dist}$o->{name}-$o->{arch}";
my $dir = "$ENV{PWD}/$tpl-$$.tmp";

sub sh {
	my ($n, $c) = @_;
	print "$n\n";
	my $l = `$c`;
	if ($?) {
		`rm -rf $dir`;
		die "FAILED\n";
	}
}

print "Creating $tpl template\n";
sh 'Fetching base system', "debootstrap --arch $o->{arch} $o->{pkgs} $o->{dist} $dir $o->{repo} 1>&2";
sh 'Setting sane permissions for /root', "chroot $dir chmod 700 /root";
sh 'Disabling root login', "chroot $dir usermod -L root";
sh 'Disabling getty', "chroot $dir sed -i -e '/getty/d' /etc/inittab";
sh 'Disabling sync() for syslog', "chroot $dir sed -i -e 's\@\\([[:space:]]\\)\\(/var/log/\\)\@\\1-\\2\@' /etc/syslog.conf";
sh 'Fixing /etc/mtab', "chroot $dir rm -f /etc/mtab && chroot $dir ln -s /proc/mounts /etc/mtab";
sh 'Disabling klogd', "LC_ALL=C chroot $dir update-rc.d -f klogd remove";
sh 'Disabling quotarpc', "LC_ALL=C chroot $dir update-rc.d -f quotarpc remove";
sh 'Disabling exim', "LC_ALL=C chroot $dir update-rc.d -f exim4 remove";
sh 'Disabling inetd', "LC_ALL=C chroot $dir update-rc.d -f inetd remove";

#rm -f /etc/ssh/ssh_host_*
#cat << EOF > /etc/rc2.d/S15ssh_gen_host_keys
##!/bin/bash
#ssh-keygen -f /etc/ssh/ssh_host_rsa_key -t rsa -N ''
#ssh-keygen -f /etc/ssh/ssh_host_dsa_key -t dsa -N ''
#rm -f \$0
#EOF
#chmod a+x /etc/rc2.d/S15ssh_gen_host_keys

if ($o->{script}) {
	print "Executing postcreate script\n";
	open(F, "|chroot $dir 2> /dev/null") and print(F <STDIN>) and close F; 
	`rm -rf $dir`, die "FAILED\n" if $?;
}

sh 'Cleaning packages', "chroot $dir apt-get clean";
sh 'Packing template', "cd $dir && tar -zcf $ENV{PWD}/$tpl.tar.gz . 2> /dev/null";
sh 'Removing temp files', "rm -rf $dir";
print "Done\n"
