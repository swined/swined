from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api import memcache

class MainPage(RequestHandler):
	def get(self):
		if not memcache.incr('tv'):
		    memcache.set('tv', 1)
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow ()' % (memcache.get('tv')))

def main():
	CGIHandler().run(WSGIApplication([
		('/', MainPage),
	], debug = True))

if __name__ == '__main__': main()