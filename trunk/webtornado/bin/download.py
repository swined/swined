#! /usr/bin/python

# Written by Bram Cohen
# Modified by Alexey Alexandrov

from BitTorrent.download import download
from threading import Event
from os.path import abspath
from sys import argv, stdout
from time import time
from pysqlite2 import dbapi2 as sqlite3
from urllib import unquote
import base64
import os

def quote(text):
	if type(text) is int:
		return text
	if type(text) is float:
		return text
	return "'%s'" % (text.replace("'", "''"))

class HeadlessDisplayer:

    def __init__(self, torrentId, dbhost, dbuser, dbpass, dbname):
	self.dict = {}
	self.torrentId = torrentId;
	self.upTotal = 0
	self.downTotal = 0
	self.lastUpdate = 0
	self.peers = 0

    def dbup(self, k, v):
	if self.dict.get(k) == v: return
	db = sqlite3.connect('/var/lib/webtornado/db.sqlite')
	cr = db.cursor()
	query = 'UPDATE torrents SET %s = %s WHERE id = %s' % (k, quote(v), quote(self.torrentId))
	print query
	cr.execute(query)
	self.dict[k] = v
	db.commit()
        cr.close()
        db.close()

    def error(self, msg):
	self.dbup('error', msg)
	print 'ERROR: %s' % (msg)

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
			r = r + ':' + str(ur) + ':' + str(dr)
			pl.append(r)
		self.dbup('peerlist', '|'.join(pl))

    def chooseFile(self, default, size, saveas, dir):
        self.dbup('output', abspath(default))
        return default

    def newpath(self, path):
	print "newpath: %s" % (path)

def run(p):
    h = HeadlessDisplayer(p[0], p[1], p[2], p[3], p[4])
    db = sqlite3.connect('/var/lib/webtornado/db.sqlite')
    cr = db.cursor()
    cr.execute('SELECT pid,outdir,torrent,up,down FROM torrents WHERE id = %s' % (quote(h.torrentId)))
    r = cr.fetchone()
    fn = '/tmp/webtornado.' + str(h.torrentId) + '.torrent'
    f = open(fn, 'wb')
    f.write(base64.b64decode(r[2]))
    f.close()
    p.append('--spew');
    p.append('1')
    p.append(fn)
    if not r:
	print 'no such torrent: %s' % (h.torrentId)
	return
    if r[0] != 0:
	print 'download is already running: %s' % (r[0])
	return
    h.dbup('pid', os.getpid())
    h.dbup('error', '')
    os.chdir(r[1])
    h.upTotal = r[3]
    h.downTotal = r[4]
    cr.close()
    db.close()
    del p[0:5]
    print "starting download"
    download(p, h.chooseFile, h.display, h.finished, h.error, Event(), 80, h.newpath)

if __name__ == '__main__':
    run(argv[1:])
