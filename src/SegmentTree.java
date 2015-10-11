import java.math.BigInteger;
import java.util.Arrays;

class SegmentTree {
    private final int mod = 1000000007;
    private final BigInteger modo = BigInteger.valueOf(mod);
    private final long a[];
    private int digit, n;

    public SegmentTree(int n) {
        this.n = n;
        digit = 0;
        while (n > (1 << digit)) {
            digit++;
        }
        a = new long[1 << (digit + 1)];
        int end = (1 << digit) + n, start = 1 << digit;
        for (int i = start; i < end; i++) {
            a[i] = i - start + 1;
        }
        for (int i = end; i < start * 2; i++) {
            a[i] = 1;
        }
        buildTree(1);
    }

    private long findLcm(int node, int left, int right, int gleft, int gright) {
        if (left <= right) {
            if (gleft >= left && gright <= right) {
                return a[node];
            } else if ((left >= gleft && gright >= left) || (right >= gleft && gright >= right)) {
                return lcm(findLcm(node << 1, left, right, gleft, (gright + gleft) / 2),
                        findLcm((node << 1) + 1, left, right, ((gright + gleft) / 2) + 1, gright));
            }
        }
        return 1;
    }

    private long buildTree(int node) {
        if ((node << 1) < a.length) {
            long lcm = lcm(buildTree(node << 1), buildTree((node << 1) + 1));
            a[node] = lcm;
            return lcm;
        } else {
            return a[node];
        }
    }

    private long lcm(long first, long second) {
        return (((first * second) % mod) * BigInteger.valueOf(gcd(first, second)).modInverse(modo).longValue()) % mod;
    }

    private long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public long handleQuery(int l, int r) {
        return findLcm(1, (1 << digit) + l - 1, (1 << digit) + r - 1, 1 << digit, (1 << (digit + 1)) - 1);
    }

    public long[] getState() {
        return Arrays.copyOfRange(a, 1 << digit, (1 << digit) + n);
    }


    @Override
    public String toString() {
        return "SegmentTree{" +
                "a=" + Arrays.toString(a) +
                '}';
    }
}