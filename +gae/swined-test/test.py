from swf import UserAgent
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		ua = UserAgent()
		ua.get('http://livejournal.com/')
#		for k,v in ua.cookies:
#			self.response.out.write('%s=%s<br>' % (k,v))
#		self.response.out.write(ua.response.content)

class FetchPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/xml; charset=utf-8'
		ua = UserAgent()
		ua.get('http://lj2mail.net.ru/friends.rss?login=%s&hash=%s' % (self.request.get('login'), self.request.get('hash')))
		self.response.out.write(ua.response.content)


def main():
	run_wsgi_app(WSGIApplication([
		('/test', MainPage),
		('/fetch', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()