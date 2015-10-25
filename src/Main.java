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

    public static void main(String args[]) throws IOException, InterruptedException {
        final InputReader br = new InputReader(System.in);
        int testCases = br.readInt();
        final Solver solver = new Solver(testCases);
        int workers = Runtime.getRuntime().availableProcessors();
        for (int t = 0; t < testCases; ) {
            final Thread threads[] = new Thread[workers];
            for (int thread = 0; t < testCases && thread < threads.length; thread++, t++) {
                final int n = br.readInt(), m = br.readInt(), s = br.readInt(), a[][] = new int[n][m];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        a[i][j] = br.readInt();
                    }
                }
                final int finalT = t;
                threads[thread] = new Thread(() -> solver.solve(a, n, m, s, finalT));
                threads[thread].start();
            }
            for (int thread = 0; thread < threads.length && threads[thread] != null; thread++) {
                threads[thread].join();
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < testCases; t++) {
            sb.append(solver.results[t]).append('\n');
        }
        System.out.println(sb);
    }
}

class Solver {
    final long results[];

    Solver(int results) {
        this.results = new long[results];
    }

    public void solve(final int a[][], final int n, final int m, final int s, final int index) {
        final long tots[][] = new long[n][m];
        long counter = 0;
        tots[0][0] = a[0][0];
        if (tots[0][0] <= s) {
            counter++;
        }
        for (int i = 1; i < n; i++) {
            tots[i][0] = a[i][0] + tots[i - 1][0];
            if (tots[i][0] <= s) {
                counter++;
            }
        }
        for (int i = 1; i < m; i++) {
            tots[0][i] = a[0][i] + tots[0][i - 1];
            if (tots[0][i] <= s) {
                counter++;
            }
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                tots[i][j] = a[i][j] + tots[i][j - 1] + tots[i - 1][j] - tots[i - 1][j - 1];
                if (tots[i][j] <= s) {
                    counter++;
                }
            }
        }
        for (int j = 1; j < m; j++) {
            for (int k = 0; k < n; k++) {
                for (int l = j; l < m; l++) {
                    if (tots[k][l] - tots[k][j - 1] <= s) {
                        counter++;
                    }
                }
            }
        }
        for (int i = 1; i < n; i++) {
            for (int k = i; k < n; k++) {
                for (int l = 0; l < m; l++) {
                    if (tots[k][l] - tots[i - 1][l] <= s) {
                        counter++;
                    }
                }
            }
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                for (int k = i; k < n; k++) {
                    for (int l = j; l < m; l++) {
                        if (tots[k][l] - tots[k][j - 1] - tots[i - 1][l] + tots[i - 1][j - 1] <= s) {
                            counter++;
                        }
                    }
                }
            }
        }
        results[index] = counter;
    }
}