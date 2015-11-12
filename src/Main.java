import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        final InputReader br = new InputReader(System.in);
        final StringBuilder stringBuilder = new StringBuilder();
        final Solver solver = new Solver();
        solver.preprocessing();
        for (int tests = br.readInt(); tests > 0; tests--) {
            stringBuilder.append(solver.solve(br.readInt())).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {
    private final int size = 10000001;
    private final int factors[][] = new int[size][7];
    private final int count[] = new int[size];

    public void preprocessing() {
        final int sqrt = (int) Math.sqrt(size + 1);
        for (int i = 4; i < size; i = i + 2) {
            factors[i][count[i]++] = 2;
        }
        for (int i = 3; i <= sqrt; i = i + 2) {
            if (count[i] == 0) {
                for (int j = i + i; j < size; j = j + i) {
                    factors[j][count[j]++] = i;
                }
            }
        }
    }

    public long solve(final int n) {
        if (count[n] == 0) {
            return n * (long) n;
        } else {
            final int occurences[] = new int[count[n]];
            for (int i = 0; i < occurences.length; i++) {
                int temp = n;
                while (temp > 1) {
                    temp = temp / factors[n][i];
                    occurences[i]++;
                }
            }

        }
    }
}

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
