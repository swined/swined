from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler
from google.appengine.api import urlfetch
import re

class LJ:
	login = None
	hpass = None
	session = None
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
		cookies = self.getCookies()
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
#		r = ''
#		for k in res.headers:
#			r = r + k + ' = ' + res.headers[k] + '<br>'
#		return r + '<hr><textarea style="width: 100%; height: 400px">' + res.content + '</textarea><hr>'
		return res.content

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		lj = LJ(self.request.get('login'), self.request.get('hash'))
		for url in lj.getList():
			self.response.out.write(lj.getEntry('http://batuich.livejournal.com/346599.html'))
			return

def main():
	run_wsgi_app(WSGIApplication([
		('/lj2rss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()