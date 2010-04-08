package nsuj03;

import java.util.ArrayList;
import java.util.List;

public class FiboSequenceGenerator implements ISequenceGenerator {

    public Long[] generate(Long max) {
        List<Long> fibo = new ArrayList<Long>();
        if (max > 0) {
            fibo.add((long)1);
            fibo.add((long)1);
        }
        if (max > 1)
        while (true) {
            Long next = fibo.get(fibo.size() - 1) + fibo.get(fibo.size() - 2);
            if (next > max)
                break;
            else
                fibo.add(next);
        }
        return fibo.toArray(new Long[0]);
    }

}
