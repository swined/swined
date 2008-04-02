#!/usr/bin/perl

use lib 'lib';
use WWW::FreeProxy;

local $\ = "\n";
fetch_proxies { print shift };