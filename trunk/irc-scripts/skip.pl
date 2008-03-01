use strict;
use Irssi;
use Irssi::Irc;

use vars qw($VERSION %IRSSI);

$VERSION = "0.1.0";
%IRSSI = (
    authors     => 'Alexey \'swined\' Alexandrov',
    contact     => 'swined@gmail.com',
    name        => 'skip',
    description => 'Skips selected types of messages and prints them to a window',
    license     => 'Public Domain',
);

sub win {
    my $win = Irssi::window_find_name('skip');
    if (!$win) {
	Irssi::command("window new hidden");
	Irssi::command("window name skip");
	$win = Irssi::window_find_name('skip');
    }
    return $win;
}

sub msg_join
{
  my ($s, $c, $n, $h) = @_;
  win->printformat(MSGLEVEL_CLIENTCRAP, 'skip_msg_join', $n, $h, $c, $s->{tag});
  Irssi::signal_stop;
}

sub msg_part
{
  my ($s, $c, $n, $h, $m) = @_;
  win->printformat(MSGLEVEL_CLIENTCRAP, 'skip_msg_part', $n, $h, $c, $s->{tag}, $m);
  Irssi::signal_stop;
}

sub msg_quit
{
  my ($s, $n, $h, $m) = @_;
  win->printformat(MSGLEVEL_CLIENTCRAP, 'skip_msg_quit', $n, $h, $s->{tag}, $m);
  Irssi::signal_stop;
}

sub msg_nick
{
  my ($s, $nn, $on, $h) = @_;
  win->printformat(MSGLEVEL_CLIENTCRAP, 'skip_msg_nick', $on, $nn, $s->{tag});
  Irssi::signal_stop;
}

sub msg_kick
{
  my ($server, $channame, $kicked, $nick, $host, $reason) = @_;
  $channame =~ s/^://;

  my $windowname = Irssi::window_find_name('highlite');
  $windowname->print("%Y%0KICK : " . $kicked . " : " . $channame . " : " . $nick . " : " . $reason, MSGLEVEL_CLIENTCRAP) if ($windowname);
}

sub sig_printtext {
  my ($dest, $text, $stripped) = @_;

  if (($dest->{level} & (MSGLEVEL_HILIGHT|MSGLEVEL_MSGS)) && ($dest->{level} & MSGLEVEL_NOHILIGHT) == 0)
  {
    if ($dest->{level} & MSGLEVEL_PUBLIC)
    {
      my $windowname = Irssi::window_find_name('highlite');

      $windowname->print("%W%0HIGHLITE : " . $dest->{target} . " : " . $text, MSGLEVEL_CLIENTCRAP) if ($windowname);
    }
  }
}

Irssi::theme_register([
    'skip_msg_join', '{line_start}{channick_hilight $0} {chanhost_hilight $1} has joined {channel $2}/{server $3}',
    'skip_msg_part', '{line_start}{channick $0} {chanhost $1} has left {channel $2}/{server $3} {reason $4}',
    'skip_msg_quit', '{line_start}{channick $0} {chanhost $1} has quit {server $2} {reason $3}',
    'skip_msg_nick', '{line_start}{channick $0} is now known as {channick_hilight $1} {comment {server $2}}',
]);

Irssi::signal_add_last(
{
  'message join' => \&msg_join,
  'message part' => \&msg_part,
  'message quit' => \&msg_quit,
#  'message topic' => \&msg_topic,
#  'print text', 'sig_printtext',
  'message nick' => \&msg_nick,
#  'message kick' => \&msg_kick,
}
);

