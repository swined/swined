# -*- Mode: Perl -*-

package Filesys::DiskSpace;

use strict;
use vars qw(@ISA @EXPORT $VERSION $DEBUG);
use Exporter;
use Config;
use Carp;
require 5.003;

@ISA = qw(Exporter);
@EXPORT = qw(df);
$VERSION = "0.05";

# known FS type numbers
my %fs_type = (
	       0	  => "4.2",			# 0x00000000
	       256	  => "UFS",			# 0x00000100
	       2560	  => "ADVFS",			# 0x00000A00
	       4989	  => "EXT_SUPER_MAGIC",		# 0x0000137D
	       4991	  => "MINIX_SUPER_MAGIC",	# 0x0000137F
	       5007	  => "MINIX_SUPER_MAGIC2",	# 0x0000138F
	       9320	  => "MINIX2_SUPER_MAGIC",	# 0x00002468
	       9336	  => "MINIX2_SUPER_MAGIC2",	# 0x00002478
	       19780	  => "MSDOS_SUPER_MAGIC",	# 0x00004d44
	       20859	  => "SMB_SUPER_MAGIC",		# 0x0000517B
	       22092	  => "NCP_SUPER_MAGIC",		# 0x0000564c
	       26985	  => "NFS_SUPER_MAGIC",		# 0x00006969
	       38496	  => "ISOFS_SUPER_MAGIC",	# 0x00009660
	       40864	  => "PROC_SUPER_MAGIC",	# 0x00009fa0
	       44543      => "AFFS_SUPER_MAGIC",        # 0x0000ADFF
	       61265	  => "EXT2_OLD_SUPER_MAGIC",	# 0x0000EF51
	       61267	  => "EXT2_SUPER_MAGIC",	# 0x0000EF53
	       72020	  => "UFS_MAGIC",		# 0x00011954
	       16914836	  => "TMPFS_MAGIC",		# 0x01021994
	       19911021	  => "_XIAFS_SUPER_MAGIC",	# 0x012FD16D
	       19920820	  => "XENIX_SUPER_MAGIC",	# 0x012FF7B4
	       19920821	  => "SYSV4_SUPER_MAGIC",	# 0x012FF7B5
	       19920822	  => "SYSV2_SUPER_MAGIC",	# 0x012FF7B6
	       19920823	  => "COH_SUPER_MAGIC",	        # 0x012FF7B7
	       827541066  => "JFS_SUPER_MAGIC",		# 0x3153464a
	       1382369651 => "REISERFS_SUPER_MAGIC",	# 0x52654973
	       1397118030 => "NTFS_SB_MAGIC",		# 0x5346544e
	       1481003842 => "XFS_SUPER_MAGIC",         # 0x58465342
	       4187351113 => "HPFS_SUPER_MAGIC",        # 0xF995E849
	       #1448756819 => "VZFS?!",
	       21318 => "VZFS?!",
);
if (0) {
    printf "%10u 0x%010x %s\n", $_, $_, $fs_type{$_}
	for sort { $a <=> $b } keys %fs_type;
    exit 1;
}

sub df ($) {
  my $dir = shift;

  my ($fmt, $res, $type, $flags);
  local $SIG{__DIE__};

  # struct fields for statfs or statvfs....
  my ($bsize, $frsize, $blocks, $bfree, $bavail, $files, $ffree, $favail);

  Carp::croak "Usage: df '\$dir'" unless $dir;
  Carp::croak "Error: $dir is not a directory" unless -d $dir;

  my $have_syscall_ph = eval {
      package main;
      require "sys/syscall.ph";
  };
  # try even if it's missing, maybe SYS_stat*() was defined by hand

  # try with statvfs..
  eval {  # will work for Solaris 2.*, OSF1 v3.2, OSF1 v4.0 and HP-UX 10.*.
    $fmt = "\0" x 512;
    $res = syscall (&main::SYS_statvfs, $dir, $fmt) ;
    ($bsize, $frsize, $blocks, $bfree, $bavail, $files, $ffree, $favail) =
      unpack "L!8", $fmt;
    # bsize:  fundamental file system block size
    # frsize: fragment size
    # blocks: total blocks of frsize on fs
    # bfree:  total free blocks of frsize
    # bavail: free blocks avail to non-superuser
    # files:  total file nodes (inodes)
    # ffree:  total free file nodes
    # favail: free nodes avail to non-superuser

    # to stay ok with statfs..
    $type = 0; # should we try to read it from the structure ?  it looks
               # possible at least under Solaris.
    $ffree = $favail;
    $bsize = $frsize;
    # $blocks -= $bfree - $bavail;
    warn "statvfs: res=$res type=$type\n" if $DEBUG;
    $res == 0 && defined $fs_type{$type};
  }
  || do { warn "statvfs failed: ", $@ if $DEBUG; 0 }
  # try with statfs..
  || eval { # will work for SunOS 4, Linux 2.0.* and 2.2.*
    $fmt = "\0" x 512;
    $res = syscall (&main::SYS_statfs, $dir, $fmt);
    # statfs...

    if ($^O eq 'freebsd') {
      # only tested with FreeBSD 3.0. Should also work with 4.0.
      my ($f1, $f2);
      ($f1, $bsize, $f2, $blocks, $bfree, $bavail, $files, $ffree) =
	unpack "L!8", $fmt;
      $type = 0; # read it from 'f_type' field ?
    }
    else {
      printf "raw L7 %s\n", join " ", unpack "L!7", $fmt
	if $DEBUG && $DEBUG > 1;
      ($type, $bsize, $blocks, $bfree, $bavail, $files, $ffree) =
	unpack "L!7", $fmt;
    }
    # type:   type of filesystem (see below)
    # bsize:  optimal transfer block size
    # blocks: total data blocks in file system
    # bfree:  free blocks in fs
    # bavail: free blocks avail to non-superuser
    # files:  total file nodes in file system
    # ffree:  free file nodes in fs

    warn "statfs: res=$res type=$type\n" if $DEBUG;
    $res == 0 && defined $fs_type{$type};
  }
  || do { warn "statfs L7 failed: ", $@ if $DEBUG; 0 }
  || eval {
    # The previous try gives an unknown fs type, it must be a different
    # structure format..
    $fmt = "\0" x 512;
    # Try this : n2i7L119
    $res = syscall (&main::SYS_statfs, $dir, $fmt);
    printf "raw n2i7 %s\n", join " ", unpack "n2i7", $fmt
      if $DEBUG && $DEBUG > 1;
    ($type, $flags, $bsize, $frsize, $blocks,
     $bfree, $bavail, $files, $ffree) = unpack "n2i7", $fmt;
    warn "statfs n2i7: res=$res type=$type\n" if $DEBUG;
    $res == 0 && defined $fs_type{$type};
  }
  || do { warn "statfs n2i7 failed: ", $@ if $DEBUG; 0 }
  # Neither statfs nor statvfs.. too bad.
  || do {
    if (!$have_syscall_ph) {
      if ($Config{'d_syscall'} eq 'define') {
	Carp::croak "sys/syscall.ph is missing, see the h2ph man page";
      }
    }
    my $syscall;
    my $extra = '';
    my $osvers = $Config{'osvers'};
    # These system normaly works but there was a problem...
    # Trying to inform the user...
    if ($^O eq 'solaris' || $^O eq 'dec_osf') {
      # Tested. No problem if syscall.ph is present.
      $syscall = 'statvfs';
    }
    elsif ($^O eq 'linux' || $^O eq 'freebsd') {
      # Tested with linux 2.0.0 and 2.2.2
      # No problem if syscall.ph is present.
      $syscall = 'statfs';
    }
    elsif ($^O eq 'hpux') {
      if ($osvers == 9) {
	# Tested. You have to change a line in syscall.ph.
	$syscall = 'statfs';
	$extra = " (if you are using a hp9000s700, see the "
	  . __PACKAGE__ . " documentation)";
      }
      elsif ($osvers == 10) {
	# Tested. No problem if syscall.ph is present.
	$syscall = 'statvfs';
      }
    }
    if ($syscall) {
      Carp::croak "$syscall failed on $dir (new filesystem type?)$extra";
    }
    Carp::croak "Cannot use df on this machine (untested or unsupported).";
  };

  $blocks -= $bfree - $bavail;

  if ($files == $ffree) {
    $files = 1;
    $ffree = 0;
  }

  warn "Warning : type $fs_type{$type} untested.. results may be incorrect\n"
    unless $type != 2560  && defined $fs_type{$type};

  if ($DEBUG) {
    warn "Fs type : [$type] $fs_type{$type}\n" .
      "total space : ", $blocks * $bsize / 1024, " Kb\n" .
      "available space : ", $bavail * $bsize / 1024, " Kb\n\n";
    if ($files == 1 && $ffree == 0) {
      warn "inodes : no information available\n";
    }
    else {
      warn "inodes : $files\nfree inodes : $ffree\n" .
	"used inodes : ", $files - $ffree, "\n";
    }
  }

  ($type, $fs_type{$type}, ($blocks - $bavail) * $bsize / 1024,
   $bavail * $bsize / 1024, $files - $ffree, $ffree);
}

1;

=head1 NAME

Filesys::DiskSpace - Perl df

=head1 SYNOPSIS

    use Filesys::DiskSpace;
    ($fs_type, $fs_desc, $used, $avail, $fused, $favail) = df $dir;

=head1 DESCRIPTION

This routine displays information on a file system such as its type, the
amount of disk space occupied, the total disk space and the number of inodes.
It tries C<syscall(SYS_statfs)> and C<syscall(SYS_statvfs)> in several ways.
If all fails, it C<croak>s.

=head1 OPTIONS

=over 4

=item $fs_type

[number] type of the filesystem.

=item $fs_desc

[string] description of this fs.

=item $used

[number] size used (in Kb).

=item $avail

[number] size available (in Kb).

=item $ffree

[number] free inodes.

=item $fused

[number] inodes used.

=back

=head1 Installation

See the INSTALL file.

=head1 COPYRIGHT

Copyright (c) 1996-1999 Fabien Tassin. All rights reserved.
This program is free software; you can redistribute it and/or
modify it under the same terms as Perl itself.

=head1 AUTHOR

Fabien Tassin E<lt>fta+cpan@sofaraway.orgE<gt>

=head1 NOTES

This module was formerly called File::Df. It has been renamed into
Filesys::DiskSpace. It could have be Filesys::Df but unfortunatly
another module created in the meantime uses this name.

Tested with Perl 5.003 under these systems :

           - Solaris 2.[4/5]
           - SunOS 4.1.[2/3/4]
           - HP-UX 9.05, 10.[1/20] (see below)
           - OSF1 3.2, 4.0
           - Linux 2.0.*, 2.2.*

Note for HP-UX users :

   if you obtain this message :
   "Undefined subroutine &main::SYS_statfs called at Filesys/DiskSpace.pm
   line XXX" and if you are using a hp9000s700, then edit the syscall.ph file
   (in the Perl lib tree) and copy the line containing "SYS_statfs {196;}"
   outside the "if (defined &__hp9000s800)" block (around line 356).

=cut
