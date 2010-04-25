package nsuj05;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static TimeStamp[] read(String fileName) throws IOException {
        FileInputStream stream = null;
        List<TimeStamp> timeStamps = new ArrayList<TimeStamp>();
        try {
            stream = new FileInputStream(fileName);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                TimeStamp timeStamp = TimeStamp.parse(line);
                if (timeStamp != null)
                    timeStamps.add(timeStamp);
            }
        } finally {
            if (stream != null)
                stream.close();
        }
        return timeStamps.toArray(new TimeStamp[0]);
    }

    public static void main(String[] args) throws Throwable {
        TimeStamp add = TimeStamp.parse(args[1]);
        for (TimeStamp timeStamp : read(args[0]))
            System.out.println(add.add(timeStamp));
    }

}
