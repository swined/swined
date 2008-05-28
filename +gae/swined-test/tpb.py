from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch

class RssPage(RequestHandler):
	def get(self, ct, qw):
		self.response.headers['Content-Type'] = 'text/xml; charset=utf-8'
		res = fetch('http://rss.thepiratebay.org/' + ct)
		self.response.out.write(res.content)

def main():
	CGIHandler().run(WSGIApplication([('/tpb/(.*)/(.*)', RssPage)], debug = True))

if __name__ == '__main__': main()