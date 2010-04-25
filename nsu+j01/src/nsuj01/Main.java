package nsuj01;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("usage $0 <num>");
            System.exit(1);
        }
        long n = 0;
        try {
            n = Long.parseLong(args[0]);
        } catch (Throwable e) {
            System.err.println("failed to parse num");
            System.exit(2);
        }
        int c = 0;
        while (n > 0) {
            c += n & 0x01;
            n = n >> 1;
        }
        System.out.println(c);
    }

}
