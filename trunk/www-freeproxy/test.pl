#!/usr/bin/perl

use lib 'lib';
use WWW::FreeProxy;

local $\ = "\n";
print for WWW::FreeProxy->plugins;