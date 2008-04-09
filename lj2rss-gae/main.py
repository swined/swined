import wsgiref.handlers

from google.appengine.api import users
from google.appengine.ext import webapp

class Page(webapp.RequestHandler):
  user = users.get_current_user()
  def authenticate(self):
    if not self.user:
      self.redirect(users.create_login_url(self.request.uri))

class FriendsPage(Page):
  def get(self):
    self.response.headers['Content-Type'] = 'text/plain'
    self.response.out.write('hellow')

class MainPage(Page):    
  def get(self):
    self.authenticate()
    self.response.headers['Content-Type'] = 'text/plain'
    self.response.out.write('Hello, ' + self.user.nickname())

def main():
  application = webapp.WSGIApplication([
      ('/', MainPage), 
      ('/friends.rss', FriendsPage)
    ])
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == "__main__":
  main()