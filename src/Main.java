import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        final InputReader br = new InputReader(System.in);
        final StringBuilder stringBuilder = new StringBuilder();
        final Solver solver = new Solver();
        for (int tests = br.readInt(); tests > 0; tests--) {
            final int n = br.readInt();
            final int price[] = new int[n];
            for (int i = 0; i < n; i++) {
                price[i] = br.readInt();
            }
            final int m = br.readInt();
            final int[][] offers = new int[m][];
            for (int i = 0; i < m; i++) {
                offers[i] = new int[br.readInt()];
                for (int j = 0; j < offers[i].length; j++) {
                    offers[i][j] = br.readInt();
                }
            }
            stringBuilder.append(solver.solve(n, price, m, offers)).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {

    public long solve(final int n, final int[] price, final int m, final int[][] offers) {
        final int offer[] = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < offers[i].length; j++) {
                offer[i] = offer[i] | (1 << (offers[i][j] - 1));
            }
        }
        Arrays.sort(offer);
        int length = 1;
        for (int i = 1; i < m; i++) {
            if (offer[i] != offer[i - 1]) {
                offer[length++] = offer[i];
            }
        }
        return 0;
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
