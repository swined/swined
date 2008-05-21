from urlfetch import fetch
from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication

class TestPage(RequestHandler):
    def get(self):
	self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
	self.response.out.write(fetch('http://ya.ru/').content)

def main(): CGIHandler().run(WSGIApplication([('/test.html', TestPage)], debug = True))
if __name__ == '__main__': main()