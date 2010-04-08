package nsuj03;

import java.util.ArrayList;
import java.util.List;

public class PrimeSequenceGenerator implements ISequenceGenerator {

    public Long[] generate(Long max) {
        if (max < 2)
            return new Long[0];
        List<Long> primes = new ArrayList<Long>();
        for (long i = 2; i <= max; i ++) {
            boolean prime = true;
            for (Long p : primes)
                if (i % p == 0) {
                    prime = false;
                    break;
                }
            if (prime)
                primes.add(i);
        }
        return primes.toArray(new Long[0]);
    }

}
