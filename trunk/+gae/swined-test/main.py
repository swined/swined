from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication

class TestPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow:')

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow')

def main():
	CGIHandler().run(WSGIApplication([
		('/', MainPage),
		('/test.html', TestPage),
	]))

if __name__ == '__main__': main()