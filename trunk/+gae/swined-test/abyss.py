from google.appengine.ext.webapp import util
from google.appengine.ext import webapp
from google.appengine.api.urlfetch import fetch
import re

class MainPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write(fetch('http://bash.org.ru/abyss').content)

def main():
	webapp.run_wsgi_app(webapp.WSGIApplication([
		('/abyss', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()