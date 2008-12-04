#!/bin/sh

svn up
~/google_appengine/appcfg.py update ~/gcode/+gae/swined-test
svn commit -m 'auto commit'