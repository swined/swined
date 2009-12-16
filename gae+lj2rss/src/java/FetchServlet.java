import cache.MemCache;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            MemCache<String, String> cache = new MemCache(new PageFetcher());
            final String url = request.getParameter("url");
            if (!new URL(url).getHost().endsWith("livejournal.com"))
                throw new Exception("only .livejournal.com urls are accepted");
            out.println(cache.get(url));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
