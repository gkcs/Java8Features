import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        final InputReader br = new InputReader(System.in);
        final StringBuilder stringBuilder = new StringBuilder();
        final Solver solver = new Solver();
        for (int tests = br.readInt(); tests > 0; tests--) {
            final int n = br.readInt(), m = br.readInt();
            final int[] a = new int[m], b = new int[m];
            for (int i = 0; i < m; i++) {
                a[i] = br.readInt();
                b[i] = br.readInt();
            }
            stringBuilder.append(solver.solve(n, a, b)).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {
    private final boolean used[] = new boolean[101];
    private final boolean adj[][] = new boolean[101][101];

    public int solve(final int n, final int[] a, final int[] b) {
        int pathCount = 0;
        int total = 0;
        for (int i = 0; i < a.length; i++) {
            adj[a[i]][b[i]] = true;
        }
        while (total < n) {
            total += findLongestPath(n);
            pathCount++;
        }
        return pathCount;
    }

    private int findLongestPath(final int n) {
        int longestPath[] = new int[]{0};
        for (int i = 1; i <= n; i++) {
            if (!used[i]) {
                final int path[] = getLongestPathFrom(i);
                if (path[0] > longestPath[0]) {
                    longestPath = path;
                }
            }
        }
        for (int i = 1; i < longestPath[0]; i++) {
            used[longestPath[i]] = true;
        }
        return longestPath.length;
    }

    private int[] getLongestPathFrom(final int root) {

        return new int[0];
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


