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
    if result.status_code == 200:
      return result.content
    else:
      return result.status_code
  def get(self):
    self.response.headers['Content-Type'] = 'text/html'
    self.w(self.login())

class MainPage(Page):
  def get(self):
    self.response.headers['Content-Type'] = 'text/html'
    self.w('<a href=/friends.rss>friends.rss</a>')

def main():
  application = WSGIApplication([
      ('/', MainPage),
      ('/friends.rss', FriendsPage)
    ])
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == "__main__":
  main()