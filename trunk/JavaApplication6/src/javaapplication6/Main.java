package javaapplication6;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://academ.org");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        InputStream stream = (InputStream)connection.getContent();
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while (null != (line = bufferedReader.readLine())) {
            System.out.println(line);
        }
    }

}
