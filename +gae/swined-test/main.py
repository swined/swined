from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from util import UserAgent
import re

class MainPage(RequestHandler):
	ua = UserAgent()
	def set_ljsession(self, ljsession):
		if not ljsession: return
		t = ljsession.split(':')
		self.ua.cookies['ljsession'] = ljsession
		self.ua.cookies['ljloggedin'] = ':'.join(t[1:2])
		return 1
	def login(self):
		data = 'mode=sessiongenerate&expiration=short&user=' + self.request.get('login') + '&hpassword=' + self.request.get('hash')
		res = self.ua.post('http://www.livejournal.com/interface/flat', data)
		if not res: return
		n = 0
		for ljsession in res.split("\n"):
			if n: return self.set_ljsession(ljsession)
			if ljsession == 'ljsession': n = 1
	def list(self):
		res = self.ua.get('http://www.livejournal.com/mobile/friends.bml?skip=' + self.request.get('skip'))
		if not res: return
		return [l.group(1) for l in re.compile(": <a href='(.*?)\?.*?'>").finditer(res)]
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		if not self.login():
			self.response.out.write('shit happened')
			return
		self.response.out.write(self.ua.get('http://elbonia.livejournal.com/1530986.html?auth=digest'))
		#for l in self.list():
		#	self.response.out.write(l + '<br>')

def main():
	CGIHandler().run(WSGIApplication([
		('/', MainPage),
	], debug = True))

if __name__ == '__main__': main()