from wsgiref import handlers
from google.appengine.ext import webapp
from google.appengine.api import urlfetch
import re

class LJ:
	login = None
	hpass = None
	session = None
	def __init__(self, login, hpass):
		self.login = login
		self.hpass = hpass
	def fetch(self, url, payload = None, method = urlfetch.GET, headers = {}, allow_truncated = False):
		res = urlfetch.fetch(url, payload, method, headers, allow_truncated)
		if res.status_code != 200:
			raise Exception('http error' + str(res.status_code) + ' (' + url + ')')
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
	def getList(self, skip = 0):
		res = self.fetch(
			'http://www.livejournal.com/mobile/friends.bml?skip=' + str(skip),
			headers = { 'Cookie' : 'ljsession=' + self.getSession() + '; ljloggedin=' + self.getLoggedIn() }
		).content
		rx = re.compile(": <a href='(http://.*?/\d+\.html)\?format=light'>")
		rr = []
		for m in rx.finditer(res):
			rr.append(m.group(1))
		return rr

class MainPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		lj = LJ(self.request.get('login'), self.request.get('hash'))
		for url in lj.getList():
			self.response.out.write(url + '<br>')

def main():
	handlers.CGIHandler().run(webapp.WSGIApplication([
		('/lj2rss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()