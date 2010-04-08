package nsuj03;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("usage $0 [fibo|prime] <N>");
        ISequenceGenerator generator = SequenceGeneratorFactory.create(args[0]);
        Long max = Long.parseLong(args[1]);
        for (Long l : generator.generate(max))
            System.out.println(l);
    }

}
