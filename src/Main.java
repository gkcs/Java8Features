import java.io.IOException;
import java.io.InputStream;
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
        final int n = reader.readInt(), m = reader.readInt(), k = reader.readInt();
        boolean board[][] = new boolean[n + 1][m + 1];
        for (int i = 0; i < k; i++) {
            board[reader.readInt()][reader.readInt()] = true;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        long sums[][][] = new long[n + 1][m + 1][k + 1];
        sums[1][0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                for (int l = 0; l <= k; l++) {
                    sums[i][j][l] = sums[i][j - 1][l] + sums[i - 1][j][l];
                }
                if (board[i][j]) {
                    for (int l = k; l > 0; l--) {
                        sums[i][j][l] = sums[i][j][l - 1];
                    }
                    sums[i][j][0] = 0;
                }
            }
        }

        long totals[] = new long[k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int l = 0; l < k; l++)
                    totals[l] = totals[l] + sums[i][j][l];
            }
        }
        for (int i = 0; i <= k; i++) {
            stringBuilder.append(totals[i]).append(' ');
        }
        System.out.println(stringBuilder.toString());
    }

}
