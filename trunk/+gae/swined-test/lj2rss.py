from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler
from google.appengine.api import memcache
import re
from swf import UserAgent

class LJ:
	ua = UserAgent()
	user = None
	hpass = None
	cmiss = 0
	maxcmiss = 5
	def __init__(self, user, hpass):
		self.user = user
		self.hpass = hpass
	def login(self):
		if self.ua.cookies.has_key('ljsession'):
			return
		res = self.ua.post('http://www.livejournal.com/interface/flat', 'mode=sessiongenerate&expiration=short&user=' + self.user + '&hpassword=' + self.hpass)
		k = None
		for v in res.split("\n"):
			if k:
				if k == 'errmsg':
					raise Exception(v)
				if k == 'ljsession':
					self.ua.cookies['ljsession'] = v
					t = v.split(':')
					self.ua.cookies['ljloggedin'] = t[1] + ':' + t[2]
					return
				k = None
			else:
				k = v
		raise Exception('logon failed')
	def getList(self, skip = 0):
		self.login()
		res = self.ua.get('http://www.livejournal.com/mobile/friends.bml?skip=' + str(skip))
		raise Exception(res)
		rx = re.compile(": <a href='(http://.*?/\d+\.html)\?format=light'>")
		rr = []
		for m in rx.finditer(res):
			rr.append(m.group(1))
		return rr
	def getEntry(self, url):
		self.login()
		mc = memcache.get(url)
		if mc:
			return mc
		if self.cmiss >= self.maxcmiss:
			return None
		self.cmiss = self.cmiss + 1
		res = self.ua.get(url + '?format=light')
		if not re.search('<blockquote>', res):
			return None
		title = re.search('<title>(.*?)</title>', res).group(1)
		res = re.compile('<blockquote>.*?</blockquote>', re.S).sub('', res)
		res = re.compile('^.*?<body >', re.S).sub('', res)
		res = re.compile('<\/body>.*?$', re.S).sub('', res)
		res = re.compile('^(.*?)<hr \/>.*?<hr \/> ', re.S).sub('\1', res)
		res = re.compile('<br style=\'clear: both\' \/>.*?$', re.S).sub('', res)
		res = '<title>' + title + '</title>' + res
		memcache.add(url, res)
		return res

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		lj = LJ(self.request.get('login'), self.request.get('hash'))
		for url in lj.getList():
			self.response.out.write(url)
			self.response.out.write('<hr>')

def main():
	run_wsgi_app(WSGIApplication([
		('/friends.rss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()