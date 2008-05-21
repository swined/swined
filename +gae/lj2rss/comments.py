from wsgiref.handlers import CGIHandler
from google.appengine.ext.webapp import RequestHandler, WSGIApplication

class CommentsPage(RequestHandler):
  def get(self):
    self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
    self.response.out.write('comments.info')

def main():
  CGIHandler().run(WSGIApplication([
    ('/comments.info', CommentsPage)
  ]))

if __name__ == '__main__': main()