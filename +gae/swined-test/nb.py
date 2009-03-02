import re
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
	def all(self):
		return Note.all().filter('user', self.user)
	def tags(self):
		tags = []
		for note in self.all():
			for tag in note.tags:
				if tag not in tags:
					tags.append(tag)
		return tags
	def list(self, tags):
		query = self.all()
		for tag in tags: query.filter('tags', tag)
		return query.order('-mtime')
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
		note = db.get(id)
		note.text = text
		note.put()
	def set_tags(self, id, tags):
		note = db.get(id)
		note.tags = tags
		note.put()
#	def share(self, id, user):
		#
#	def unshare(self, id, user):
		#

def exml(text):
	txt = re.compile('&', re.S).sub('&amp;', text)
	txt = re.compile('<', re.S).sub('&lt;', txt)
	txt = re.compile('>', re.S).sub('&gt;', txt)
	return txt

def ftag(name, value):
	return '<%s>%s</%s>' % (exml(name), exml(value), exml(name))

class MainPage(RequestHandler):
        def get(self):
                self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		nb = Notebook(users.get_current_user())
		for note in nb.list(['1', '5', '>_<']):
			self.response.out.write(str(note.key()) + ': ' + note.text + '<br>')
			for tag in note.tags: self.response.out.write(' +' + tag)
			self.response.out.write('<br>')
		self.response.out.write('---<br>')
		self.response.out.write('<tags>')
		for tag in nb.tags():
			self.response.out.write(ftag('tag', tag))
		self.response.out.write('</tags>')


def main():
        run_wsgi_app(WSGIApplication([
                ('/nb', MainPage),
        ], debug = True))

if __name__ == '__main__':
        main()

