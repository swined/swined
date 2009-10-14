import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UA ua = new UA();
        try {
            ua.GET(new URL("http://swined.livejournal.com/"));
            response.getOutputStream().print(ua.toString());
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    } 

}
