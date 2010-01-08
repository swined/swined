package bmg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import logger.ILogger;

public class Main {

    public static void main(String[] args) throws Exception {
        Logger h = Logger.getLogger("com.wombat.nose");
        h.severe("hello world");
        //ILogger logger = LogManager.getLogger();
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://nameserver3.info/bookmark";
        Connection con = DriverManager.getConnection(url, "bookmark", "");
        System.out.println("URL: " + url);
        System.out.println("Connection: " + con);
        Statement stmt = con.createStatement();
        ResultSet r = stmt.executeQuery("SELECT * FROM queue LIMIT 1");
        //logger.debug(r.getString("owner"));
        con.close();
    }
}
