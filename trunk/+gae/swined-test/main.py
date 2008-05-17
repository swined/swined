from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from xml.sax.saxutils import XMLGenerator

class TestPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/xml; charset=utf-8'
		xml = XMLGenerator()
		xml.startDocument()
		xml.startElement('rss', {})
		xml.characters('test')
		xml.endElement('rss')
		xml.endDocument()
		self.response.out.write(xml.output())
		

class MainPage(RequestHandler):
	def get(self):
		self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
		self.response.out.write('hellow')

def main():
	CGIHandler().run(WSGIApplication([
		('/', MainPage),
		('/test.html', TestPage),
	], debug = True))

if __name__ == '__main__': main()