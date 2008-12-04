from swf import UserAgent
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow')

def main():
	run_wsgi_app(WSGIApplication([
		('/test', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()