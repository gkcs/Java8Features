import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        final InputReader br = new InputReader(System.in);
        final StringBuilder stringBuilder = new StringBuilder();
        final Solver solver = new Solver();
        for (int tests = br.readInt(); tests > 0; tests--) {
            final String pixels = br.readString();
            int photo = 0;
            for (int i = 0; i < pixels.length(); i++) {
                if (pixels.charAt(i) == 'w') {
                    photo |= 1 << i;
                }
            }
            final int n = br.readInt();
            final int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                final String filter = br.readString();
                for (int j = 0; j < filter.length(); j++) {
                    if (filter.charAt(j) == '+') {
                        a[i] |= 1 << j;
                    }
                }
            }
            stringBuilder.append(solver.solve(a, photo)).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {
    private static final int mod = 1000000007;

    public void printArray(int a[]) {
//        printArray(new int[]{photo});
//        printArray(a);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (final int number : a) {
            stringBuilder.append(String.format("%10s", Long.toBinaryString(number)).replace(' ', '0')).append(',').append(' ');
        }
        stringBuilder.append(']');
        System.out.println(stringBuilder);
    }

    public long solve(final int[] a, final int photo) {
        final int occurences[] = new int[1024];
        for (final int filter : a) {
            occurences[filter]++;
        }
        final long subsets[] = new long[1024];
        for (int i = 0; i < occurences.length; i++) {
            long power = powMod(occurences[i] - 1);
            for (int j = 0; j < i; j++) {
                subsets[i ^ j] = (subsets[i ^ j] * power) % mod;
                subsets[j] = (subsets[j] * (power - 1)) % mod;
            }
            subsets[i] = (subsets[i] + power) % mod;
            subsets[0] = (subsets[0] + power) % mod;
        }
        return subsets[photo];
    }

    public long powMod(int exp) {
        if (exp <= 0) {
            return 1;
        } else if (exp < 60) {
            return 1L << exp;
        } else {
            final long powers[] = new long[25];
            long result = 1;
            powers[0] = 2;
            for (int i = 1; i < 25; i++) {
                powers[i] = (powers[i - 1] * powers[i - 1]) % mod;
            }
            for (int i = 0; i < 25; i++) {
                if ((exp & (1 << i)) != 0) {
                    result = (result * powers[i]) % mod;
                }
            }
            return result;
        }
    }
}

class InputReader {
    private final InputStream stream;
    private final byte[] buf = new byte[1024];

    private int curChar;

    private int numChars;

    public InputReader(InputStream stream) {
        this.stream = stream;
    }

    public int read() {
        if (numChars == -1)
            throw new InputMismatchException();
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (numChars <= 0)
                return -1;
        }
        return buf[curChar++];
    }

    public int readInt() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public boolean isSpaceChar(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

    public String readString() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        StringBuilder res = new StringBuilder();
        do {
            res.appendCodePoint(c);
            c = read();
        }
        while (!isSpaceChar(c));
        return res.toString();
    }

    public long readLong() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        long res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        }
        while (!isSpaceChar(c));
        return res * sgn;
    }


}


