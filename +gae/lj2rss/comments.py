from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.ext import db
from google.appengine.api.urlfetch import fetch
from google.appengine.api import memcache
from png import PNGCanvas
from time import time
import datetime
import re

class Request(db.Model):
    time = db.DateTimeProperty(auto_now_add = True)
    service = db.StringProperty()

class Font:
    width = 0
    height = 0
    chars = ''
    canvas = None
    def __init__(self, width, height, file, chars):
	self.width = width
	self.height = height
	self.chars = chars
	self.canvas = PNGCanvas(0, 0)
	f = open(file, 'rb')	
	self.canvas.load(f)
	f.close()

font = Font(12, 12, 'font.png', '0123456789 abcdefghijklmnopqrstuvwxyz')
imc = {}

class TextImage:
    rc = None
    def __init__(self, text):
	global imc
	global font
	if imc.has_key(text):
	    self.rc = imc[text]
	else:
	    c = PNGCanvas(font.width * (len(text) + 1), font.height + 5)
	    for i in range(0, len(text)):
		x = 2 + self.sc(text[i])
	        font.canvas.copyRect(x, 5, x + font.width, 5 + font.height, 5 + font.width * i, 3, c)
	    imc[text] = c
	    self.rc = c
    def sc(self, s):
	global font
	r = font.chars.rfind(s)
	if r == -1: 
	    return font.width * 10
	return font.width * r
    def dump(self, stream):
	stream.write(self.rc.dump())

class CommentsPngPage(RequestHandler):
    def comments(self, user, itemid):
	res = None
	try: 
	    res = fetch('http://m.lj.ru/read/user/%s/%s' % (user, itemid))
	except: 
	    return 'shit happened'
	if res.status_code != 200: 
	    return 'error ' + str(res.status_code)
	rx = re.compile('<a href="/read/user/%s/%s/comments#comments">\S+? \((\d+)\)</a>' % (user, itemid))
	rm = rx.search(res.content)
	if rm: 
	    return rm.group(1) + ' comments'
	else: 	
	    return 'no comments'
    def get(self):
	k = 'requests_' + str(int(time() / 60))
	if not memcache.incr(k):
	    memcache.set(k, 1, 60 * 60 * 48)
        Request(service = 'comments.png').put()
	self.response.headers['Content-Type'] = 'image/png'
	TextImage(self.comments(self.request.get('user'), self.request.get('itemid'))).dump(self.response.out)

class StatsPage(RequestHandler):
    def stats(self):
	r = []
    	for i in range(1, 4 * 60):
	    q = db.Query(Request)
	    q.filter('time > ', datetime.datetime.now() - datetime.timedelta(minutes = i * 8))
	    q.filter('time < ', datetime.datetime.now() - datetime.timedelta(minutes = (i - 1) * 8))
	    r.append(str(q.count()))
	r.reverse()
	return r
    def stats1(self):
	r = []
	t = int(time() / 60)
    	for i in range(0, 60):
    	    k = 'requests_' + str(t - i)
    	    v = memcache.get(k)
    	    if not v:
    		v = 0
	    r.append(str(v))
	r.reverse()
	return r	
    def max(self, a):
	m = 0
	for e in a:
	    if int(e) > m:
		m = int(e)
	return m
    def get(self):
        Request(service = 'stats.html').put()
	self.response.headers['Content-Type'] = 'text/html'
	s = self.stats()
	S = []
	m = int(self.max(s) * 1.25)
	for i in s:
	    S.append(str(int(100 * int(i) / m)))
	c = 'cht=lc&chs=480x160&chg=10,25&chxt=y&chxr=0,0,' + str(m) + '&chd=t:' + ','.join(S)
	self.response.out.write('<img src="http://chart.apis.google.com/chart?' + c + '"><br>')
#	s = self.stats1()
#	m = self.max(s)
#	c = 'cht=lc&chs=480x160&chg=10,25&chxt=x,y&chxt=x,y&chxr=0,0,24|1,0,' + str(m) + '&chd=t:' + ','.join(s)
#	self.response.out.write('<img src="http://chart.apis.google.com/chart?' + c + '"><br>')

app = WSGIApplication([('/comments.png', CommentsPngPage), ('/stats.html', StatsPage)], debug = True)

def main(): 
    global app
    CGIHandler().run(app)

if __name__ == '__main__': 
    main()