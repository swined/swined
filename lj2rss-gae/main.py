import wsgiref.handlers

from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch, POST

class Page(RequestHandler):
  def w(self, text):
    self.response.out.write(text)
  def p(self, name):
    return self.request.get(name)

class FriendsPage(Page):
  def login(self):
    data = 'mode=sessiongenerate&expiration=short&user=' + self.p('login') + '&hpassword=' + self.p('hash')
    result = fetch('http://www.livejournal.com/interface/flat', data, POST)
    if result.status_code != 200: return
    n = 0
    for ljsession in result.content.split("\n"):
      if n: 
        if not ljsession: return
	t = ljsession.split(':')
	ljloggedin = t[1] + ':' + t[2]
	return { 'Cookie' : 'ljsession=' + ljsession + '; ljloggedin=' + ljloggedin + ';' }
      if ljsession == 'ljsession': n = 1
  def list(self, cookies):
    url = 'http://www.livejournal.com/mobile/friends.bml?skip=' + self.p('skip')
    result = fetch(url, headers = cookies)
    if result.status_code != 200: return
    return result.content
  def get(self):
    self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
    cookies = self.login()
    if not cookies: return self.w('shit happened')
    self.w(self.list(cookies))

class MainPage(Page):
  def get(self):
    self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
    self.w('<a href=/friends.rss>friends.rss</a>')

def main():
  application = WSGIApplication([
      ('/', MainPage),
      ('/friends.rss', FriendsPage)
    ])
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()