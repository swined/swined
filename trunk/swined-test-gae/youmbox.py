from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication

class UploadPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('youmbox/upload')

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('youmbox')

def main():
	CGIHandler().run(WSGIApplication([
		('/youmbox', MainPage),
		('/youmbox/upload', UploadPage),
	]))

if __name__ == '__main__': main()