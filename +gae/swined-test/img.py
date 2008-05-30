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
		for i in q:
		    self.response.out.write('<img src="http://x29.ru/i/p/%s"><br>' % (i.id))
		self.response.out.write("""
<form action="/i/upload" enctype="multipart/form-data" method="post">
    <div><label>Message:</label></div>
    <div><textarea name="comment" rows="3" cols="60"></textarea></div>
    <div><label>Image:</label></div>
    <div><input type="file" name="image"/></div>
    <div><input type="submit" value="Upload"></div>
</form>""")

class UploadPage(webapp.RequestHandler):
	def post(self):
		i = Image()
		i.owner = users.get_current_user()
		i.image = self.request.get('image')
		i.comment = self.request.get('comment')
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
		('/i/upload', UploadPage),		
	], debug = True))

if __name__ == '__main__': 
	main()