import wsgiref.handlers
from google.appengine.ext import webapp

class UploadPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('youmbox/upload')

class MainPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('youmbox')

def main():
	application = webapp.WSGIApplication([
		('/youmbox', MainPage),
		('/youmbox/upload', UploadPage),
	])
	wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__': main()