package nsuj05;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeStamp {

    private final static Pattern pattern 
            = Pattern.compile("^\\+?(-?)([0-9]+):([0-9]+):([0-9]+),([0-9]+)$");
    private final static int[] size = new int[] { 0, 24, 60, 60, 1000 };
    private final int[] timeStamp;
    
    public TimeStamp(int hh, int mm, int ss, int ms) {
        int[] ts = new int[] { 0, hh, mm, ss, ms };
        for (int i = 4; i > 0; i--) {
            while (ts[i] < 0) {
                ts[i] += size[i];
                ts[i - 1] -= 1;
            }
            ts[i - 1] += ts[i] / size[i];
            ts[i] = ts[i] % size[i];
        }
        this.timeStamp = ts;
    }

    public static TimeStamp parse(String str) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            int[] t = new int[4];
            for (int i = 0; i < 4; i++) {
                t[i] = Integer.parseInt(matcher.group(i + 2));
                if (matcher.group(1).equals("-"))
                    t[i] *= -1;
            }
            return new TimeStamp(t[0], t[1], t[2], t[3]);
        } else {
            return null;
        }
    }

    public TimeStamp add(TimeStamp ts) {
        int[] t = new int[4];
        for (int i = 0; i < 4; i++)
            t[i] = ts.timeStamp[i+1] + timeStamp[i+1];
        return new TimeStamp(t[0], t[1], t[2], t[3]);
    }

    @Override
    public String toString() {
        return timeStamp[1] + ":" + timeStamp[2]
                + ":" + timeStamp[3] + "," + timeStamp[4];
    }

}
