package org.swined.dsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.swined.dsa.integer.IInteger;
import org.swined.dsa.util.Buffer;
import org.swined.dsa.util.DsaPublicKey;

public class Main {

    private static IExpression[] var(int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) {
            e[i] = new Var(i, false);
        }
        return e;
    }

    private static BigInteger extract(int l, Map<Integer, Const> s) {
    	if (s == null)
    		return null;
        BigInteger r = BigInteger.ZERO;
        for (int v : s.keySet()) {
            if (v < l) {
                if (s.get(v) == Const.ONE) {
                    r = r.setBit(v);
                }
            }
        }
        return r;
    }

    private static IExpression eq(IExpression[] e, BigInteger n) {
        IExpression r = Const.ONE;
        for (int i = 0; i < e.length; i++) {
            IExpression x = n.testBit(i) ? e[i] : Bin.not(e[i]);
            r = Bin.and(r, x);
        }
        return r;
    }
    
    private static Map<Integer, Const> solve(IExpression eq) {
        if (eq == Const.ONE) {
            return new HashMap<Integer, Const>();
        }
        if (eq == Const.ZERO) {
            return null;
        }
        int var = eq.getVars().getLowestSetBit();
        for (Const c : Const.values()) {
            Map<Integer, Const> s = solve(eq.sub(var, c, new HashMap<IExpression, IExpression>()));
            if (s != null) {
                s.put(var, c);
                return s;
            }
        }
        return null;
    }

    // a * x = b + p * y => (a - b mod a) mod (p mod a) = 0
    
    private final static String PUBLIC_KEY = "AAAAB3NzaC1kc3MAAACBAOKrUButGeIr3JqgEcHFDf3ixuFXVPEAxcO1351TbcWwtJGQWTr7s/JjAH4//KIJ/Jz4sHBTV+puU9WcaFQqSDpJj4yrp5BDhrWXyKHvqXYxpClC98gX7wC/AFLFvBSfkC9z26ID/ZA6xZ/vxnLRSVvT7Kk4GPIVlNL7IlD5LpNdAAAAFQC2MFVm8JMwY5XbbkJp14TDNOAHRQAAAIEA2jeKEjqDUMSAlXpV6ZnKeqYfIXpUg0jAQ7QI+ScuVMLjWIByLbpZuF6SBrmGkmIZ8X8e6O7rRwhj5DcevFH6lwz/e8BWl/7VeYBmSZEgSJbVVaUn1osKG0hVIekktKkodYsGyWKxycJRGvWkyYxfSat+NgiHGpcOEsf0kZ+/lpEAAACBAJ+siyR6dbk44mjS0725KeolAge7sEbp9PCFkuJ4tIVxbKaSMUzNIYP2fmyxbrtMcvC5KmTZe9GuM45Jpj6/PFB9eaBA2LSlHojFRtM129O5Dphlfca8GEInGd0t0iciRNpK4cYh6I+Rx88y/HF7EpJF1o/ptmvRnYpmbSm8R483";
    private final static String PRIVATE_KEY = "MIIBvQIBAAKBgQDiq1AbrRniK9yaoBHBxQ394sbhV1TxAMXDtd+dU23FsLSRkFk6+7PyYwB+P/yiCfyc+LBwU1fqblPVnGhUKkg6SY+Mq6eQQ4a1l8ih76l2MaQpQvfIF+8AvwBSxbwUn5Avc9uiA/2QOsWf78Zy0Ulb0+ypOBjyFZTS+yJQ+S6TXQIVALYwVWbwkzBjldtuQmnXhMM04AdFAoGBANo3ihI6g1DEgJV6VemZynqmHyF6VINIwEO0CPknLlTC41iAci26Wbhekga5hpJiGfF/Huju60cIY+Q3HrxR+pcM/3vAVpf+1XmAZkmRIEiW1VWlJ9aLChtIVSHpJLSpKHWLBsliscnCURr1pMmMX0mrfjYIhxqXDhLH9JGfv5aRAoGBAJ+siyR6dbk44mjS0725KeolAge7sEbp9PCFkuJ4tIVxbKaSMUzNIYP2fmyxbrtMcvC5KmTZe9GuM45Jpj6/PFB9eaBA2LSlHojFRtM129O5Dphlfca8GEInGd0t0iciRNpK4cYh6I+Rx88y/HF7EpJF1o/ptmvRnYpmbSm8R483AhUAgBewJWUfpXocdKNtr7rWnVcOK4Y=";
    
    public static void main(String[] args) {
    	DsaPublicKey publicKey = new Buffer(DatatypeConverter.parseBase64Binary(PUBLIC_KEY)).readDsaPublicKey();
		int pkl = publicKey.guessPrivateKeyBitLength();
		IInteger mp = Int.modPow(publicKey.g, var(1), publicKey.p);
		System.out.println(mp);
		System.out.println(Arrays.toString(mp.getBits()));
    }
    
}
