import wsgiref.handlers
import re

from logging import debug
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch, GET, POST

class UserAgent:
  cookies = {}
  def cookie_string(self): return '; '.join(['%s=%s' % (k, v) for k, v in self.cookies.items()])
  def get(self, url): return self.request(url)
  def post(self, url, data): return self.request(url, data, POST)
  def request(self, url, data = None, method = GET):
    res = fetch(url, data, method, { 'Cookie' : self.cookie_string() })
    if res.status_code == 200: return res.content

class Page(RequestHandler):
  def w(self, text): self.response.out.write(text)
  def p(self, name): return self.request.get(name)

class FriendsPage(Page):
  ua = UserAgent()
  def login(self):
    data = 'mode=sessiongenerate&expiration=short&user=' + self.p('login') + '&hpassword=' + self.p('hash')
    res = self.ua.post('http://www.livejournal.com/interface/flat', data)
    if not res: return
    n = 0
    for ljsession in res.split("\n"):
      if n: 
        if not ljsession: return
	t = ljsession.split(':')
	self.ua.cookies['ljsession'] = ljsession
	self.ua.cookies['ljloggedin'] = ':'.join(t[1:2])
	debug(self.ua.cookie_string())
	return 1
      if ljsession == 'ljsession': n = 1
  def list(self):
    res = self.ua.get('http://www.livejournal.com/mobile/friends.bml?skip=' + self.p('skip'))
    if not res: return
    return [l.group(1) for l in re.compile(": <a href='(.*?)\?.*?'>").finditer(res)]
  def entry(self, url):
    # check cache
    res = self.ua.get(url + '?format=light')
    if not res: return
    # update cache
    return { 'content' : res }
  def get(self):
    self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
    if not self.login(): return self.w('shit happened')
    for url in self.list(): 
      entry = self.entry(url)
      if not entry: return self.w('no entry: ' + url)
#      return self.w(entry['content'])

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