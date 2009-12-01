import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            URL url = new URL("http://mirdya.livejournal.com/data/rss?auth=digest");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setInstanceFollowRedirects(false);
            final String login = request.getParameter("login");
            final String password = request.getParameter("password");
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication (
                        login,
                        password.toCharArray());
                }
            });
//            LJ lj = new LJ();
//            lj.login(request.getParameter("login"), request.getParameter("hash"));
//            out.println(lj.toString());
//            out.println("links:");
//            for (String link : lj.links())
//                out.println(link + "<br>" + lj.getEntry(link) + "<hr>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    } 

}
