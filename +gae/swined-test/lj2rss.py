from wsgiref import handlers
from google.appengine.ext import webapp
from google.appengine.api import urlfetch
from google.appengine.api import memcache

class LJ:
	login = None
	hpass = None
	def __init__(self, login, hpass):
		self.login = login
		self.hpass = hpass
	def fetch(self, url, payload = None, method = urlfetch.GET, headers = {}, allow_truncated = False):
		res = urlfetch.fetch(url, payload, method, headers, allow_truncated)
		if res.status_code != 200:
			raise Exception('http error' + str(res.status_code) + ' (' + url + ')')
		return res
	def uid(self):
		return self.login + self.hpass
	def getSession(self):
		ses = memcache.get('session_' + self.uid()):
		if ses is not None:
			return 'cached: ' + ses
		res = self.fetch(
			'http://www.livejournal.com/interface/flat', 
			'mode=sessiongenerate&user=' + self.login + '&hpassword=' + self.hpass + '&expiration=short',
			urlfetch.POST,
		).content
		k = None
		for v in res.split("\n"):
			if k:
				if k == 'errmsg':
					raise Exception(v)
				if k == 'ljsession':
					memcache.set('session_' + self.uid(), v)
					return v
				k = None
			else:
				k = v
	def dropSession(self):
		memcache.delete('session_' + self.uid())

class MainPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		lj = LJ(self.request.get('login'), self.request.get('hash'))
		self.response.out.write(lj.getSession())

def main():
	handlers.CGIHandler().run(webapp.WSGIApplication([
		('/lj2rss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()