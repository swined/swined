from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch

class YouTube:
	def getFlvLink(self, url):
		res = fetch(url)
		if res.status_code == 200: return res.content

class UploadPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		youTube = YouTube()
		flvLink = youTube.getFlvLink(self.request.get('url'))
		if not flvLink: return self.response.out.write('shit happened')
		self.response.out.write(flvLink)

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('<a href=/youmbox/upload>upload</a>')

def main():
	CGIHandler().run(WSGIApplication([
		('/youmbox', MainPage),
		('/youmbox/upload', UploadPage),
	]))

if __name__ == '__main__': main()