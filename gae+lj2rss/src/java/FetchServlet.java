import cache.MemCache;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchServlet extends HttpServlet {

    private byte[] replace(byte[] a, byte[] b) {
        int c = 0;
        for (int i = 0; i < a.length - b.length; i++) {
            if (a[i] == b[c])
                c++;
            if (c == b.length) {
                byte[] r = new byte[a.length - b.length];
                for (int j = 0; j < i - c; j++)
                    r[j] = a[j];
                for (int j = i; j < a.length; j++)
                    r[j - c] = a[j];
                return r;
            }
        }
        return a;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-type", "text/html; charset=utf-8");
            MemCache<String, byte[]> cache = new MemCache<String, byte[]>(new PageFetcher());
            final String url = request.getParameter("url");
            //if (!new URL(url).getHost().endsWith("livejournal.com"))
              //  throw new Exception("only .livejournal.com urls are accepted");
            byte[] r = cache.get(url);
            if (r == null)
                throw new Exception("failed to fetch");
            r = replace(r, "<meta name=\"robots\" content=\"noindex, nofollow, noarchive\" />".getBytes());
            response.getOutputStream().write(r);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
