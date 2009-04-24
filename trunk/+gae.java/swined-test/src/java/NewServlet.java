import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewServlet extends HttpServlet {

    private static void writeString(OutputStream stream, String string) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        writer.write(string);
    }

    private static String urlencode(String value) {
        return value; //TODO urlencode
    }

    private static String urlencode(String arg0, String arg1) {
        return urlencode(arg0) + "=" + urlencode(arg1);
    }

    private static String urlencode(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        Boolean first = true;
        for (String key : data.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(urlencode(key, data.get(key)));
        }
        return sb.toString();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<pre>[vk]");
            URL url = new URL("http://vkontakte.ru/login.php");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            OutputStream stream = connection.getOutputStream();
            Map<String, String> data = new Hashtable();
            data.put("try_to_login", "1");
            data.put("success_url", "");
            data.put("fail_url", "");
            //data.put("op", "a_login_attempt");
            data.put("expire", "0");
            data.put("email", request.getParameter("email"));
            data.put("pass", request.getParameter("pass"));
            out.println("sending: " + urlencode(data));
            writeString(stream, urlencode(data));
            stream.close();
            connection.connect();
            for (String s : connection.getHeaderFields().get("set-cookie")) {
                String[] c = s.split(";")[0].split("=", 2);
                if (c.length == 2) {
                    out.println(c[0] + " == " + c[1]);
                } else {
                    out.println("# weird cookie: " + s);
                }
            }
            out.println(new StringReader(connection).readAll());
        } catch (Exception e) {
            out.println("shit happened: " + e.getMessage());
        } finally {
            out.println("</pre>");
        }
        out.close();
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
