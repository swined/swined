from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication
from google.appengine.ext import db

class Request(db.Model):
    time = db.DateTimeProperty(auto_now_add = True)
    service = db.StringProperty()

class StatsPage(RequestHandler):
    def get(self):
	self.response.headers['Content-Type'] = 'text/html'
	self.response.out.write('hellow')

app = WSGIApplication([('/stats.html', StatsPage)])

def main(): 
    global app
    CGIHandler().run(app)

if __name__ == '__main__': 
    main()