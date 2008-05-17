from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.mail import send_mail
from google.appengine.api.urlfetch import fetch

class MainPage(RequestHandler):
	def get(self, text):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write(fetch('http://lj2rss.net.ru/friends.rss?login=swined&hash=' + text).content)
#		send_mail("swined@gmail.com", "null@x29.ru", "test", text)

def main():
	CGIHandler().run(WSGIApplication([
		('/(.*)', MainPage),
	], debug = True))

if __name__ == '__main__': main()