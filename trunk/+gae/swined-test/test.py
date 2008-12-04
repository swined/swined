from swf import UserAgent
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		ua = UserAgent()
		ua.get('http://academ.org/')
		for k,v in ua.cookies:
			self.response.out.write('%s=%s<br>' % (k,v))
		self.response.out.write(ua.response.content)

def main():
	run_wsgi_app(WSGIApplication([
		('/test', MainPage),
	], debug = True))

if __name__ == '__main__': 
	main()