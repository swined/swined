import wsgiref.handlers
from google.appengine.ext import webapp

class MainPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hell o world')

def main():
	application = webapp.WSGIApplication([
		('/', MainPage),
	])
	wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__': main()