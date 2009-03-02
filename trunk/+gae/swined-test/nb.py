from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler

class Note(db.Model):
	mtime = db.DateTimeProperty(auto_now = True)	
	user = db.ListProperty(users.User)
	text = db.TextProperty()
	tags = db.StringListProperty()

class Notebook():
	user = None
	def __init__(self, user):
		self.user = user
#	def tags(self):
		#
	def list(self, tags):
		query = Note.all().filter('user = ', self.user)
		for tag in tags: query.filter('tags = ', tag)
		return query.order('mtime')
	def add(self, text, tags):
		note = Note(
			user = [ self.user ],
			text = text, 
			tags = tags,
		)
		note.put()
		return note
	def delete(self, id):
		db.get(id).delete()
	def set_text(self, id, text):
		for note in Note.all().filter('user = ', self.user).filter('key = ', id):
			note.text = text
			note.put()
#	def set_tags(self, id, tags):
		#		
#	def share(self, id, user):
		#
#	def unshare(self, id, user):
		#

class MainPage(RequestHandler):
        def get(self):
                self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		nb = Notebook(users.get_current_user())
		note = nb.add('test', ['1', '2', '3'])
		nb.set_text(note.key(), 'teZZd')
		#nb.set_tags(note.key(), ['4', '5', '6'])
		for note in nb.list([]): self.response.out.write(str(note.key()) + ': ' + note.text + '<br>')
		self.response.out.write('test')
		
def main():
        run_wsgi_app(WSGIApplication([
                ('/nb', MainPage),
        ], debug = True))

if __name__ == '__main__':
        main()
