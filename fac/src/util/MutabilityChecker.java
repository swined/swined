package util;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MutabilityChecker {

    private static HashSet<Class> canExtend = new HashSet();
    private static HashSet<Class> canHold = new HashSet();

    static {
        canExtend.add(Object.class);
        canHold.add(String.class);
    }

    public static void check(Class c) throws Exception {
        List<Class> checked = new ArrayList();
        List<Class> toCheck = new ArrayList();
        toCheck.add(c);
        while (!toCheck.isEmpty()) {
            Class x = toCheck.remove(0);
            if (checked.contains(x)) {
                continue;
            }
            for (Field f : x.getDeclaredFields()) {
                if (!Modifier.isFinal(f.getModifiers())) {
                    throw new Exception(f + " is not final");
                }
                if (!canHold.contains(f.getType())) {
                    if (!Modifier.isFinal(f.getType().getModifiers())) {
                        throw new Exception(f.getType() + " is not final");
                    } else {
                        toCheck.add(f.getType());
                    }
                }
            }
            checked.add(x);
            if (x.getSuperclass() != null) {
                if (!canExtend.contains(x.getSuperclass())) {
                    toCheck.add(x.getSuperclass());
                }
            }
        }
    }
}
