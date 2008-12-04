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
		logging.debug('requesting: ' + url)
		logging.debug('cookies: ' + self.__cookies())
		self.response = urlfetch.fetch(url, payload, method, { 'Cookie' : self.__cookies() }, False, False)
		if self.response.headers.has_key('Set-Cookie'):
			logging.debug('set cookie: ' + self.response.headers['Set-Cookie'])
			c = self.response.headers['Set-Cookie'].split(';')
			p = c[0].split('=')
			self.cookies[p[0]] = p[1]
		if self.response.headers.has_key('Location'):
			logging.debug('location: ' + self.response.headers['Location'])
			return self.request(self.response.headers['Location'], method, payload)
		if self.response.status_code != 200:
			raise Exception('http error ' + str(self.response.status_code) + ' at ' + url)
		return self.response.content
	def __cookies(self):
		r = ''
		for k in self.cookies:
			r = '%s=%s; %s' % (k, self.cookies[k], r)
		return r