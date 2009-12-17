
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(UploadServlet.class.getName());

    private byte[] readAll(InputStream stream) throws IOException {
        List<Byte> r = new ArrayList();
        while (stream.available() > 0) {
            byte[] b = new byte[1024];
            int c = stream.read(b);
            for (int i = 0; i < c; i++) {
                r.add(b[i]);
            }
        }
        byte[] b = new byte[r.size()];
        for (int i = 0; i < r.size(); i++) {
            b[i] = r.get(i);
        }
        return b;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            response.setContentType("text/plain");
            FileItemIterator iterator = upload.getItemIterator(request);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();

                if (item.isFormField()) {
                    log.warning("Got a form field: " + item.getFieldName());
                } else {
                    log.warning("Got an uploaded file: " + item.getFieldName() +
                            ", name = " + item.getName());
                    Image img = ImagesServiceFactory.makeImage(readAll(stream));
                    PersistenceManager pm = PMF.get().getPersistenceManager();
                    UserService userService = UserServiceFactory.getUserService();

                    Pic p = new Pic(userService.getCurrentUser(), img);
                    try {
                        pm.makePersistent(p);
                    } finally {
                        pm.close();
                    }
                    response.getWriter().write(p.getId() + "\n");

                }

            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
