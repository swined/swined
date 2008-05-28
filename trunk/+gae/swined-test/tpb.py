from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch

class RssPage(RequestHandler):
	def get(self, qw):
		self.response.headers['Content-Type'] = 'text/xml; charset=utf-8'
		res = fetch('http://thepiratebay.org/rss')
		self.response.out.write(res.content)

def main():
	CGIHandler().run(WSGIApplication([('/tpb/(.*)', RssPage)], debug = True))

if __name__ == '__main__': main()