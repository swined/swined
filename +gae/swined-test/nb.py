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
	def list(self, tags, excl):
		query = self.all()
		for tag in tags: query.filter('tags', tag)
		for tag in excl: query.filter('tags != ', tag)
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
		note = db.get(id)
		if self.user not in note.user: return
		if 'trash' not in note.tags:
			note.tags.append('trash')
		note.put()
	def set_text(self, id, text):
		note = db.get(id)
		if self.user not in note.user: return
		note.text = text
		note.put()
	def set_tags(self, id, tags):
		note = db.get(id)
		if self.user not in note.user: return
		note.tags = tags
		note.put()
#	def share(self, id, user):
		#
#	def unshare(self, id, user):
		#

class MainPage(RequestHandler):
	def exml(self, text):
		txt = re.compile('&', re.S).sub('&amp;', text)
		txt = re.compile('<', re.S).sub('&lt;', txt)
		txt = re.compile('>', re.S).sub('&gt;', txt)
		return txt
	def ftag(self, name, value):
		return '<%s>%s</%s>' % (self.exml(name), self.exml(value), self.exml(name))
	def farr(self, name, value):
		text = ''
		for v in value:
			text = text + self.ftag(name, v)
		return text
	def get(self, action):
		nb = Notebook(users.get_current_user())
		if action == 'delete':
			nb.delete(self.request.get('id'))
		if action in ['']:
			nb.add('test', ['x', 'y', 'z'])
			self.response.headers['Content-Type'] = 'text/xml; charset=utf-8'
			self.response.out.write('<?xml version="1.0" encoding="UTF-8"?>')
			self.response.out.write('<?xml-stylesheet href="/static/notes.xsl" type="text/xsl"?>')
			self.response.out.write('<nb>')
			for note in nb.list([], ['trash']):
				self.response.out.write(
					'<note>' +
						self.ftag('id', str(note.key())) +
						self.ftag('text', note.text) +
						self.farr('tag', note.tags) +
						self.ftag('mtime', str(note.mtime)) +
					'</note>'
				)
			self.response.out.write('<tags>' + self.farr('tag', nb.tags()) + '</tags>')
			self.response.out.write('</nb>')


def main():
        run_wsgi_app(WSGIApplication([
                ('/nb/(.*)', MainPage),
        ], debug = True))

if __name__ == '__main__':
        main()

