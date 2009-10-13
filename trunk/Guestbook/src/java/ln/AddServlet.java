package ln;

import java.io.IOException;
import javax.servlet.http.*;

public class AddServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        URL url = URL.create(req.getParameter("url"));
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, " + req.getServerName() + "/" + url.getKey() + " : " + url.getLink());
    }
}
