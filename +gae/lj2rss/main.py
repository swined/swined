import wsgiref.handlers
import datetime
import re

from logging import debug
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.api.urlfetch import fetch, GET, POST
from google.appengine.ext import db
from Cookie import SimpleCookie

class Entry(db.Model):
    url = db.StringProperty(required=True)
    title = db.StringProperty(required=True)
    content = db.TextProperty(required=True)
    created = db.DateProperty()

class UserAgent:
    cookies = {}
    def cookie_string(self): 
	return '; '.join(['%s=%s' % (k, v) for k, v in self.cookies.items()])
    def parseCookies(self, cookie):
	if cookie == '': return
	c = SimpleCookie()
	c.load(cookie)
	for k in c.keys(): 
	    self.cookies[k] = c.get(k).value
    def get(self, url): 
	return self.request(url)
    def post(self, url, data): 
	return self.request(url, data, POST)
    def request(self, url, data = None, method = GET):
	res = fetch(url, data, method, { 'Cookie' : self.cookie_string() })
	if res.headers.has_key('Set-Cookie'): 
	    self.parseCookies(res.headers['Set-Cookie'])
	if res.status_code == 200: 
	    return res.content

class Page(RequestHandler):
  def w(self, text): self.response.out.write(text)
  def p(self, name): return self.request.get(name)

class FriendsPage(Page):
  ua = UserAgent()
  def set_ljsession(self, ljsession):
    if not ljsession: return
    t = ljsession.split(':')
    self.ua.cookies['ljsession'] = ljsession
    self.ua.cookies['ljloggedin'] = ':'.join(t[1:2])
    return 1
  def login(self):
    data = 'mode=sessiongenerate&expiration=short&user=' + self.p('login') + '&hpassword=' + self.p('hash')
    res = self.ua.post('http://www.livejournal.com/interface/flat', data)
    if not res: return
    n = 0
    for ljsession in res.split("\n"):
      if n: return self.set_ljsession(ljsession)
      if ljsession == 'ljsession': n = 1
  def list(self):
    res = self.ua.get('http://www.livejournal.com/mobile/friends.bml?skip=' + self.p('skip'))
    if not res: return
    return [l.group(1) for l in re.compile(": <a href='(.*?)\?.*?'>").finditer(res)]
  def entry(self, url):
    for e in Entry.all().filter('url = ', url).fetch(1): return { 'url' : e.url, 'title' : e.title, 'content' : e.content, 'cached' : 1 }
    res = self.ua.get(url + '?format=light')
    if not res: return
    Entry(url = url, title = '--', content = db.Text(res, encoding = 'utf-8')).put()
    return { 'url' : url, 'content' : res, 'title' : '--', 'cached' : 0 }
  def get(self):
    self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
    if not self.login(): return self.w('shit happened')
    c = 0
    for url in self.list(): 
      entry = self.entry(url)
      if not entry: return self.w('no entry: ' + url)
#      self.w(entry['content'])
      self.w(entry['url'])
      self.w('<br>')
      if not entry['cached']: c = c + 1
      if c == 5: return
    self.w('# done')
    self.w(self.ua.cookie_string)

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