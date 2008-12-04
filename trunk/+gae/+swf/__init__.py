from google.appengine.api import urlfetch

class UserAgent:
	cookies = {}
	def get(self, url):
		return self.request(url)
	def post(self, url, payload):
		return self.request(url, urlfetch.POST, payload)
	def request(self, url, method = urlfetch.GET, payload = None):
		r = urlfetch.fetch(url, payload, method, { 'Cookie' : self.__cookieString() }, False, False)
		if res.headers.has_key('Set-Cookie'):
			for cookie in res.headers['Set-Cookie'].split(';'):
				k,v = cookie.split('=')
				cookies[k] = v
		if r.headers.has_key('Location'):
			return self.request(r.headers['Location'], method, payload)
		if r.status_code != 200:
			raise Exception('http error ' + str(r.status_code) + ' at ' + url)
		return r
	def __cookieString(self):
		r = ''
		for k,v in self.cookies:
			r = '%s=%s; %s' % (k, v, r)
		return r