from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication

class TestPage(RequestHandler):
	def get(self, a, b):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow: ' + a + ' / ' + b)

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow')

def main():
	CGIHandler().run(WSGIApplication([
		('/', MainPage),
		('/test', TestPage),
	]))

if __name__ == '__main__': main()