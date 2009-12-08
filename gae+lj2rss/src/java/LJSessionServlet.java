import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LJSessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            Cache cache = cacheFactory.createCache(Collections.emptyMap());
            String login = request.getParameter("login");
            String hash = request.getParameter("hash");
            String key = "ljsession_" + login + "_" + hash;
            boolean nocache = request.getParameter("nocache") != null;
            Object r = cache.get(key);
            if ((r == null) || nocache) {
                LJ lj = new LJ();
                lj.login(login, hash);
                r = lj.toString();
                cache.put(key, r);
            }
            out.println(r);
        } catch (Exception e) {
            out.println("error:");
            e.printStackTrace(out);
        }
    }

}
