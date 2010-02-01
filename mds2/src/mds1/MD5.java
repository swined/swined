package mds1;

public class MD5 {

    private static final Exp32 A = new Exp32(0x67452301L);
    private static final Exp32 B = new Exp32(0xefcdab89L);
    private static final Exp32 C = new Exp32(0x98badcfeL);
    private static final Exp32 D = new Exp32(0x10325476L);

    private static final int S11 = 7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 = 5;
    private static final int S22 = 9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 = 4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 = 6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;

    private static Exp32 F(Exp32 x, Exp32 y, Exp32 z) {
        return x.and(y).or(x.not().and(z));
    }

    private static Exp32 G(Exp32 x, Exp32 y, Exp32 z) {
        return x.and(z).or(y.and(z.not()));
    }

    private static Exp32 H(Exp32 x, Exp32 y, Exp32 z) {
        return x.xor(y).xor(z);
    }

    private static Exp32 I(Exp32 x, Exp32 y, Exp32 z) {
        return z.not().or(x).xor(y);
    }

    private static Exp32 FF(Exp32 a, Exp32 b, Exp32 c, Exp32 d, Exp32 mj, int s, long ti) {
        return a.sum(F(b, c, d)).sum(mj).sum(new Exp32(ti)).shift(s).sum(b);
    }

    private static Exp32 GG(Exp32 a, Exp32 b, Exp32 c, Exp32 d, Exp32 mj, int s, long ti) {
        return a.sum(G(b, c, d)).sum(mj).sum(new Exp32(ti)).shift(s).sum(b);
    }

    private static Exp32 HH(Exp32 a, Exp32 b, Exp32 c, Exp32 d, Exp32 mj, int s, long ti) {
        return a.sum(H(b, c, d)).sum(mj).sum(new Exp32(ti)).shift(s).sum(b);
    }

    private static Exp32 II(Exp32 a, Exp32 b, Exp32 c, Exp32 d, Exp32 mj, int s, long ti) {
        return a.sum(I(b, c, d)).sum(mj).sum(new Exp32(ti)).shift(s).sum(b);
    }

    public static Exp32[] transform(Exp32 data[]) {
        if (data.length > 13)
            throw new UnsupportedOperationException("at most 13 data elements expected");
        Exp32 a = A;
        Exp32 b = B;
        Exp32 c = C;
        Exp32 d = D;
        
        Exp32 x[] = new Exp32[16];
        for (int i = 0; i < data.length; i++)
            x[i] = data[i];
        x[data.length] = new Exp32(0x00000080L);
        for (int i = data.length + 1; i < 14; i++)
            x[i] = new Exp32(0);
        x[15] = new Exp32(0);
        x[14] = new Exp32(data.length * 32);

        a = FF(a, b, c, d, x[0], S11, 0xd76aa478L);
        d = FF(d, a, b, c, x[1], S12, 0xe8c7b756L);
        c = FF(c, d, a, b, x[2], S13, 0x242070dbL);
        b = FF(b, c, d, a, x[3], S14, 0xc1bdceeeL);
        a = FF(a, b, c, d, x[4], S11, 0xf57c0fafL);
        d = FF(d, a, b, c, x[5], S12, 0x4787c62aL);
        c = FF(c, d, a, b, x[6], S13, 0xa8304613L);
        b = FF(b, c, d, a, x[7], S14, 0xfd469501L);
        a = FF(a, b, c, d, x[8], S11, 0x698098d8L);
        d = FF(d, a, b, c, x[9], S12, 0x8b44f7afL);
        c = FF(c, d, a, b, x[10], S13, 0xffff5bb1L);
        b = FF(b, c, d, a, x[11], S14, 0x895cd7beL);
        a = FF(a, b, c, d, x[12], S11, 0x6b901122L);
        d = FF(d, a, b, c, x[13], S12, 0xfd987193L);
        c = FF(c, d, a, b, x[14], S13, 0xa679438eL);
        b = FF(b, c, d, a, x[15], S14, 0x49b40821L);

        a = GG(a, b, c, d, x[1], S21, 0xf61e2562L);
        d = GG(d, a, b, c, x[6], S22, 0xc040b340L);
        c = GG(c, d, a, b, x[11], S23, 0x265e5a51L);
        b = GG(b, c, d, a, x[0], S24, 0xe9b6c7aaL);
        a = GG(a, b, c, d, x[5], S21, 0xd62f105dL);
        d = GG(d, a, b, c, x[10], S22, 0x2441453L);
        c = GG(c, d, a, b, x[15], S23, 0xd8a1e681L);
        b = GG(b, c, d, a, x[4], S24, 0xe7d3fbc8L);
        a = GG(a, b, c, d, x[9], S21, 0x21e1cde6L);
        d = GG(d, a, b, c, x[14], S22, 0xc33707d6L);
        c = GG(c, d, a, b, x[3], S23, 0xf4d50d87L);
        b = GG(b, c, d, a, x[8], S24, 0x455a14edL);
        a = GG(a, b, c, d, x[13], S21, 0xa9e3e905L);
        d = GG(d, a, b, c, x[2], S22, 0xfcefa3f8L);
        c = GG(c, d, a, b, x[7], S23, 0x676f02d9L);
        b = GG(b, c, d, a, x[12], S24, 0x8d2a4c8aL);

        a = HH(a, b, c, d, x[5], S31, 0xfffa3942L);
        d = HH(d, a, b, c, x[8], S32, 0x8771f681L);
        c = HH(c, d, a, b, x[11], S33, 0x6d9d6122L);
        b = HH(b, c, d, a, x[14], S34, 0xfde5380cL);
        a = HH(a, b, c, d, x[1], S31, 0xa4beea44L);
        d = HH(d, a, b, c, x[4], S32, 0x4bdecfa9L);
        c = HH(c, d, a, b, x[7], S33, 0xf6bb4b60L);
        b = HH(b, c, d, a, x[10], S34, 0xbebfbc70L);
        a = HH(a, b, c, d, x[13], S31, 0x289b7ec6L);
        d = HH(d, a, b, c, x[0], S32, 0xeaa127faL);
        c = HH(c, d, a, b, x[3], S33, 0xd4ef3085L);
        b = HH(b, c, d, a, x[6], S34, 0x4881d05L);
        a = HH(a, b, c, d, x[9], S31, 0xd9d4d039L);
        d = HH(d, a, b, c, x[12], S32, 0xe6db99e5L);
        c = HH(c, d, a, b, x[15], S33, 0x1fa27cf8L);
        b = HH(b, c, d, a, x[2], S34, 0xc4ac5665L);

        a = II(a, b, c, d, x[0], S41, 0xf4292244L);
        d = II(d, a, b, c, x[7], S42, 0x432aff97L);
        c = II(c, d, a, b, x[14], S43, 0xab9423a7L);
        b = II(b, c, d, a, x[5], S44, 0xfc93a039L);
        a = II(a, b, c, d, x[12], S41, 0x655b59c3L);
        d = II(d, a, b, c, x[3], S42, 0x8f0ccc92L);
        c = II(c, d, a, b, x[10], S43, 0xffeff47dL);
        b = II(b, c, d, a, x[1], S44, 0x85845dd1L);
        a = II(a, b, c, d, x[8], S41, 0x6fa87e4fL);
        d = II(d, a, b, c, x[15], S42, 0xfe2ce6e0L);
        c = II(c, d, a, b, x[6], S43, 0xa3014314L);
        b = II(b, c, d, a, x[13], S44, 0x4e0811a1L);
        a = II(a, b, c, d, x[4], S41, 0xf7537e82L);
        d = II(d, a, b, c, x[11], S42, 0xbd3af235L);
        c = II(c, d, a, b, x[2], S43, 0x2ad7d2bbL);
        b = II(b, c, d, a, x[9], S44, 0xeb86d391L);

        return new Exp32[] { a.sum(A), b.sum(B), c.sum(C), d.sum(D) };
    }

}
