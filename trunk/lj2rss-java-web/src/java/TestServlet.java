import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=utf-8");
        try {
            LJ lj = new LJ();
            lj.login(request.getParameter("login"), request.getParameter("hash"));
            out.println("links:");
            for (String link : lj.links())
                out.println(link + "<br>" + lj.getEntry(link) + "<hr>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

}
