from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch

class RssPage(RequestHandler):
	def fetch(self, url):
		res = fetch(url)
		if res.status_code != 200: 
		        raise Exception('http error ' + str(res.status_code))
		return res.content
	def get(self, ct, qw):
		self.response.headers['Content-Type'] = 'text/xml; charset=utf-8'
		self.response.out.write(self.fetch('http://rss.thepiratebay.org/' + ct))

def main():
	CGIHandler().run(WSGIApplication([('/tpb/(.*)/(.*)', RssPage)], debug = True))

if __name__ == '__main__': main()