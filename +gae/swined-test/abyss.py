from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler
from google.appengine.api.ulfetch import fetch
import re

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write(fetch('http://bash.org.ru/abyss').content)

def main():
	run_wsgi_app(WSGIApplication([
		('/abyss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()