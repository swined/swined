import logging
from google.appengine.api import urlfetch

class UserAgent:
	cookies = {}
	response = None
	def get(self, url):
		return self.request(url, urlfetch.GET, None)
	def post(self, url, payload):
		return self.request(url, urlfetch.POST, payload)
	def request(self, url, method, payload):
		logging.debug('requesting ' + url)
		self.response = urlfetch.fetch(url, payload, method, { 'Cookie' : self.__cookieString() }, False, False)
		for k,v in self.response.headers:
			logging.debug('header: %s=%s' % (k,v))
		if self.response.headers.has_key('Set-Cookie'):
			logging.debug('set cookie: ' + self.response.headers['Set-Cookie'])
			for cookie in self.response.headers['Set-Cookie'].split(';'):
				p = cookie.split('=')
				raise Exception('set cookie: %s=%s' % (p[0],p[1]))
				self.cookies[p[0]] = p[1]
		if self.response.headers.has_key('Location'):
			logging.debug('redirecting to ' + self.response.headers['Location'])
			return self.request(self.response.headers['Location'], method, payload)
		if self.response.status_code != 200:
			raise Exception('http error ' + str(self.response.status_code) + ' at ' + url)
		return self.response
	def __cookieString(self):
		r = ''
		for k,v in self.cookies:
			r = '%s=%s; %s' % (k, v, r)
		return r