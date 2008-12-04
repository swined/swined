from google.appengine.api import urlfetch

class UserAgent:
	cookies = {}
#	def get(self, url):
		# 
#	def post(self, url, data):
		#
#	def request(self, url, data, method):
		#
	def __cookieString(self):
		r = ''
		for k,v in self.cookies:
			r = '%s=%s; %s' % (k, v, r)
		return r