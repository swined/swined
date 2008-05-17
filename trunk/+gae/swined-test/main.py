from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication

class MainPage(RequestHandler):
	def get(self, text):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow: ' + text)

def main():
	CGIHandler().run(WSGIApplication([
		('/(.*)', MainPage),
	], debug = True))

if __name__ == '__main__': main()