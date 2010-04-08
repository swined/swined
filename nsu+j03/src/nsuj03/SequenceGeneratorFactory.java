package nsuj03;

public class SequenceGeneratorFactory {

    static ISequenceGenerator create(String name) {
        if ("fibo".equals(name))
            return new FiboSequenceGenerator();
        if ("prime".equals(name))
            return new PrimeSequenceGenerator();
        throw new UnsupportedOperationException("unknown generator '" + name + "'");
    }

}