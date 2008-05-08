from wsgiref import handlers
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
	handlers.CGIHandler().run(webapp.WSGIApplication([
		('/youmbox', MainPage),
		('/youmbox/upload', UploadPage),
	]))

if __name__ == '__main__': main()