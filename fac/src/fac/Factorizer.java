package fac;

import java.util.ArrayList;
import java.util.List;

public class Factorizer {

    public List<Tuple<Byte, Byte>> factorize(Byte x) {
        List<Tuple<Byte, Byte>> r = new ArrayList();
        for (int i = 0; i < 0x100; i++)
            for (int j = 0; j < 0x100; j++)
                if (i * j % 0x100 == x)
                    r.add(new Tuple(i, j));
        return r;
    }

    public List<FactorizationTask> factorize(FactorizationTask task) {
        List<FactorizationTask> r = new ArrayList<FactorizationTask>();
        for (Tuple<Byte, Byte> pair : factorize(task.getHead().getDigit(0))) {
           byte overflow = (byte)((pair.getX() + pair.getY()) >> 8);
           BigInt head = task.getHead().shift().substract(overflow);
           BigInt tail1 = task.getTail1().append(pair.getX());
           BigInt tail2 = task.getTail2().append(pair.getY());
           r.add(new FactorizationTask(head, tail1, tail2));
        }
        return r;
    }

}
