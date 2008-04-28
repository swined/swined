#! /usr/bin/python

# Written by Bram Cohen
# Modified by Alexey Alexandrov

from BitTorrent.download import download
from threading import Event
from os.path import abspath
from sys import argv, stdout
from time import time

import MySQLdb
import os
from urllib import unquote

class HeadlessDisplayer:

    def __init__(self, torrentId, dbhost, dbuser, dbpass, dbname):
	self.dict = {}
	self.torrentId = torrentId;
	self.db = MySQLdb.connect(host = dbhost, user = dbuser, passwd = dbpass, db = dbname)
        self.cr = self.db.cursor()
	self.upTotal = 0
	self.downTotal = 0
	self.lastUpdate = 0
	self.peers = 0

    def dbup(self, k, v):
	if self.dict.get(k) != v:
	    self.cr.execute('UPDATE torrents SET ' + k + ' = %s WHERE id = %s',
		(v, self.torrentId))
	    self.dict[k] = v

    def error(self, msg):
	self.dbup('error', msg)

    def finished(self):
	self.dbup('progress', 100)

    def display(self, dict):
	if time() - self.lastUpdate < 15: return
	self.lastUpdate = time()
	if dict.has_key('fractionDone'): self.dbup('progress', int(dict.get('fractionDone') * 100))
	self.dbup('eta', int(dict.get('timeEst') or 0))
	self.dbup('downrate', int(dict.get('downRate') or 0))
        self.dbup('uprate', int(dict.get('upRate') or 0))
	if int(dict.get('upRate') or 0) or int(dict.get('downRate') or 0):
		self.dbup('error', '')
	upTotal = float(dict.get('upTotal') or 0)
	if upTotal: self.dbup('up', self.upTotal + upTotal)
	downTotal = float(dict.get('downTotal') or 0)
	if downTotal: self.dbup('down', self.downTotal + downTotal)
	if dict.has_key('spew'):
		spew = dict['spew']
		c = 0
		for t in spew:
			c = c + 1
		self.dbup('peers', c)
		pl = []
		for p in spew:
			r = p['ip'] + ':'
			ur, ui, uc = p['upload']
			dr, di, dc, ds = p['download']
			if ur or dr: r = r + 'r'
			pl.append(r)
		self.dbup('peerlist', '|'.join(pl))

    def chooseFile(self, default, size, saveas, dir):
	self.cr.execute('UPDATE torrents SET output = %s WHERE id = %s',
	    (abspath(default), self.torrentId))
        return default

    def newpath(self, path):
	print "newpath: %s" % (path)

def run(p):
    h = HeadlessDisplayer(p[0], p[1], p[2], p[3], p[4])
    h.cr.execute('SELECT pid,outdir,torrent,up,down FROM torrents WHERE id = %s', (h.torrentId))
    r = h.cr.fetchone()
    fn = '/tmp/webtornado.' + str(r[0]) + '.torrent'
    f = open(fn, 'w')
    f.write(unquote(r[2]))
    f.close()
    p.append('--spew');
    p.append('1')
    p.append(fn)
    if not r:
	print "no such torrent: %s" % (h.torrentId)
	return
    if r[0] != 0:
	print "download is already running: %s" % (r[0])
	return
    h.cr.execute('UPDATE torrents SET pid = %s, error = "" WHERE id = %s', (os.getpid(), h.torrentId));
    os.chdir(r[1])
    h.upTotal = r[3]
    h.downTotal = r[4]
    del p[0:5]
    download(p, h.chooseFile, h.display, h.finished, h.error, Event(), 80, h.newpath)
    h.cr.close
    h.db.close

if __name__ == '__main__':
    run(argv[1:])
