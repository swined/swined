package ln;

import java.io.IOException;
import javax.servlet.http.*;

public class GoServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        URL url = URL.load(req.getPathInfo().substring(1));
        if (url == null) {
            resp.sendRedirect("/index.html");
        } else {
            resp.sendRedirect(url.getLink());
        }
    }
}
