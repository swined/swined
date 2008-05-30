from wsgiref.handlers import CGIHandler
from google.appengine.ext import webapp
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.api import images

class Image(db.Model):
	owner = db.UserProperty()
	image = db.BlobProperty()
	comment = db.StringProperty(multiline = True)
	created = db.DateTimeProperty(auto_now_add = True)
	accessed = db.DateTimeProperty(auto_now = True)
	requests = db.IntegerProperty()

class UserPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('images')
		q = Image.all()
		q.filter('user = ', users.get_current_user())
		for i in q.fetch():
		    self.response.out.write('<img src="http://x29.ru/i/p/%s"><br>' % (i.id))
	def post(self):
		i = Image()
		i.owner = users.get_current_user()
		i.image = self.request.get('image')
		i.put()
		self.redirect('/i/u')

class MainPage(webapp.RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow')

def main():
	CGIHandler().run(webapp.WSGIApplication([
		('/i', MainPage),
		('/i/u', UserPage),
	], debug = True))

if __name__ == '__main__': 
	main()