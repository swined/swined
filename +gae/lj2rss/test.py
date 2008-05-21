from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.runtime import RPC

class TestPage(RequestHandler):
    def cb():
	self.response.out.write('callback')
    def afetch(self, url):
	rpc = RPC()
	req = None
	res = None
	rpc.MakeCall('urlfetch', 'Fetch', req, res)
	self.response.out.write('call')
	rpc.Wait();
    def get(self):
	self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
	afetch('http://x29.ru/')

def main(): CGIHandler().run(WSGIApplication([('/test.html', TestPage)], debug = True))
if __name__ == '__main__': main()