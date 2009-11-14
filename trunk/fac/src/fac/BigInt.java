package fac;

public class BigInt {

    private final byte[] digits;

    public BigInt() {
        digits = new byte[0];
    }

    public BigInt(byte digit) {
        digits = new byte[1];
        digits[0] = digit;
    }

    public BigInt(byte[] digits) {
        this.digits = digits;
    }

    public int getDigitCount() {
        return digits.length;
    }

    public byte getDigit(int ix) {
        if (ix < 0)
            return 0;
        if (ix >= getDigitCount())
            return 0;
        return digits[ix];
    }

    public BigInt substract(byte x) {
        return this.substract(new BigInt(x));
    }

    public BigInt substract(BigInt x) {
        if (getDigitCount() < x.getDigitCount())
            return x.substract(this);
        byte[] r = new byte[getDigitCount() + 1];
        int overflow = 0;
        for (int i = 0; i <= getDigitCount(); i++) {
            r[i] = (byte)(getDigit(i) - x.getDigit(i) + overflow);
            overflow = (getDigit(i) - x.getDigit(i) + overflow) >> 8;
        }
        return new BigInt(r);
    }

    public BigInt shift() {
        if (getDigitCount() == 0)
            return new BigInt((byte)0);
        byte[] r = new byte[getDigitCount() - 1];
        for (int i = 1; i < getDigitCount(); i++)
            r[i - 1] = getDigit(i);
        return new BigInt(r);
    }

    public BigInt append(byte x) {
        byte[] r = new byte[getDigitCount() + 1];
        for (int i = 0; i < getDigitCount(); i++)
            r[i] = getDigit(i);
        r[r.length - 1] = x;
        return new BigInt(r);
    }

    @Override
    public String toString() {
        String r = "";
        for (int i = getDigitCount() - 1; i >= 0; i--) {
            if (!r.isEmpty())
                r += " ";
            r += Byte.toString(getDigit(i));
        }
        return "{ " + r + " }";
    }

}
