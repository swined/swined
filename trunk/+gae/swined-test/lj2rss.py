from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler
from google.appengine.api import urlfetch
from google.appengine.api import memcache
import re

class LJ:
	login = None
	hpass = None
	session = None
	cmiss = 0
	maxcmiss = 5
	def __init__(self, login, hpass):
		self.login = login
		self.hpass = hpass
	def fetch(self, url, payload = None, method = urlfetch.GET, headers = {}, allow_truncated = False, follow_redirects = False):
		res = urlfetch.fetch(url, payload, method, headers, allow_truncated, follow_redirects)
		if res.status_code != 200:
			raise Exception('http error ' + str(res.status_code) + ' (' + url + ')')
		return res
	def getSession(self):
		if self.session is not None:
			return self.session
		res = self.fetch(
			'http://www.livejournal.com/interface/flat', 
			'mode=sessiongenerate&expiration=short&user=' + self.login + '&hpassword=' + self.hpass,
			urlfetch.POST,
		).content
		k = None
		for v in res.split("\n"):
			if k:
				if k == 'errmsg':
					raise Exception(v)
				if k == 'ljsession':
					self.session = v
					return v
				k = None
			else:
				k = v
	def getLoggedIn(self):
		ses = self.getSession()
		t = ses.split(':')
		return t[1] + ':' + t[2]
	def getCookies(self):
		return 'ljsession=' + self.getSession() + '; ljloggedin=' + self.getLoggedIn() + '; '
	def getList(self, skip = 0):
		res = self.fetch(
			'http://www.livejournal.com/mobile/friends.bml?skip=' + str(skip),
			headers = { 'Cookie' : self.getCookies() }
		).content
		rx = re.compile(": <a href='(http://.*?/\d+\.html)\?format=light'>")
		rr = []
		for m in rx.finditer(res):
			rr.append(m.group(1))
		return rr
	def getEntry(self, url):
		mc = memcache.get(url)
		if mc:
			return mc
		if self.cmiss >= self.maxcmiss:
			return None
		self.cmiss = self.cmiss + 1
		cookies = self.getCookies()
		ourl = url
		url = url + '?format=light'
		res = None
		while True:
			res = urlfetch.fetch(url, headers = { 'Cookie' : cookies }, follow_redirects = False)
			if res.headers.has_key('Set-Cookie'):
				t = res.headers['Set-Cookie'].split(';')
				cookies = cookies + '; ' + t[0]
			if not res.headers.has_key('Location'):
				break
			else:
				url = res.headers['Location']
		res = res.content
		if not re.search('<blockquote>'):
			return None
		title = re.search('<title>(.*?)</title>', res).group(1)
		res = re.compile('<blockquote>.*?</blockquote>', re.S).sub('', res)
		res = re.compile('^.*?<body >', re.S).sub('', res)
		res = re.compile('<\/body>.*?$', re.S).sub('', res)
		res = re.compile('^(.*?)<hr \/>.*?<hr \/> ', re.S).sub('\1', res)
		res = re.compile('<br style=\'clear: both\' \/>.*?$', re.S).sub('', res)
		res = '<title>' + title + '</title>' + res
		memcache.add(ourl, res)
		return res

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		lj = LJ(self.request.get('login'), self.request.get('hash'))
		for url in lj.getList():
			self.response.out.write(lj.getEntry(url) + '<hr>')

def main():
	run_wsgi_app(WSGIApplication([
		('/lj2rss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()