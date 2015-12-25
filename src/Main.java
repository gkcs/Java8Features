import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        final InputReader br = new InputReader(System.in);
        final Solver solver = new Solver();
        final int q = br.readInt(), S1 = br.readInt(), a = br.readInt(), b = br.readInt();
        System.out.println(solver.solve(q, S1, a, b));
    }
}

class Solver {

    private static final long MOD = (1L << 32) - 1;

    public long solve(int q, int s1, int a, int b) {
        if ((s1 & 1) == 0 || (a & 1) == 0) {
            if ((b & 1) == 0) {
                return (s1 & 1) == 0 ? 0 : s1;
            } else {
                return calculate(q, s1, a, b);
            }
        } else {
            if ((b & 1) == 0) {
                return calculate(q, s1, a, b);
            } else {
                return s1;
            }
        }
    }

    private long calculate(int q, long s1, long a, long b) {
        long total = (s1 & 1) == 0 ? 0 : s1;
        for (int i = 0; i < q - 1; i++) {
            s1 = (a * s1 + b) & MOD;
            total = total + ((s1 & 1) == 0 ? 0 : s1);
        }
        return total;
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


