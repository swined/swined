from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import WSGIApplication, RequestHandler

class Note(db.Model):
	owner = db.ListProperty(users.User)
	mtime = db.DateTimeProperty(auto_now = True)
	tags = db.StringListProperty()
	text = db.TextProperty()

class Notebook():
	user = None
	def __init__(self, user):
		self.user = user
#	def tags(self):
		#
#	def list(self, tags):
		#
	def add(self, text, tags):
		Note(
			owner = self.user,
			text = text, 
			tags = tags,
		).put();
#	def del(self, id):
#		Note()
#	def edit(self, id, text, tags):
		#
#	def share(self, id, user):
		#
#	def unshare(self, id, user):
		#

class MainPage(RequestHandler):
        def get(self):
                self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		nb = Notebook(users.get_current_user())
		nb.add('test', ['1', '2', '3'])
		self.response.out.write('test')
		
def main():
        run_wsgi_app(WSGIApplication([
                ('/nb', MainPage),
        ], debug = True))

if __name__ == '__main__':
        main()

