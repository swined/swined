#!/bin/sh

svn commit -m 'auto commit'
svn up
~/google_appengine/appcfg.py update ~/gcode/+gae/swined-test