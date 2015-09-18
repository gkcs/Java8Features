import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;


class InputReader {
    private InputStream stream;
    private byte[] buf = new byte[1024];
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

public class Main {

    public static void main(String args[]) throws IOException {
        final InputReader reader = new InputReader(System.in);
        final Solver solver = new Solver();
        final StringBuilder stringBuilder = new StringBuilder();
        final int n = reader.readInt(), a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = reader.readInt();
        }
        stringBuilder.append(Arrays.toString(solver.solve(a))).append('\n');
        System.out.println(stringBuilder);
    }
}

class Solver {

    public int[] solve(final int a[]) {
        final int b[] = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            int max = Integer.MIN_VALUE, index = -1;
            for (int j = 0; j < a.length; j++) {
                if (max < a[j]) {
                    max = a[j];
                    index = j;
                }
            }
            b[b.length - 1 - i] = max;
            a[index] = Integer.MIN_VALUE;
        }
        return b;
    }

}
