from google.appengine.api import urlfetch

class UserAgent:
	cookies = {}
	response = None
	def get(self, url):
		return self.request(url)
	def post(self, url, payload):
		return self.request(url, urlfetch.POST, payload)
	def request(self, url, method = urlfetch.GET, payload = None):
		r = urlfetch.fetch(url, payload, method, { 'Cookie' : self.__cookieString() }, False, False)
		self.response = r
		if r.headers.has_key('Set-Cookie'):
			raise Exception('set cookie: ' . r.headers['Set-Cookie'])
			for cookie in r.headers['Set-Cookie'].split(';'):
				p = cookie.split('=')
				raise Exception('set cookie: %s=%s' % (p[0],p[1]))
				self.cookies[p[0]] = p[1]
		if r.headers.has_key('Location'):
			raise Exception('redirect to ' . r.headers['Location'])
			return self.request(r.headers['Location'], method, payload)
		if r.status_code != 200:
			raise Exception('http error ' + str(r.status_code) + ' at ' + url)
		return r
	def __cookieString(self):
		r = ''
		for k,v in self.cookies:
			r = '%s=%s; %s' % (k, v, r)
		return r