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
            final int counts[] = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                counts[i] = br.readInt();
            }
            final int[] t = new int[m], a = new int[m], b = new int[m];
            for (int i = 0; i < m; i++) {
                t[i] = br.readInt();
                a[i] = br.readInt();
                b[i] = br.readInt();
            }
            stringBuilder.append(solver.solve(n, counts, m, t, a, b)).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {

    public long solve(final int n, int[] counts, final int m, final int[] t, final int[] a, final int[] b) {
        final Offer[] offers = new Offer[m << 1];
        for (int i = 0; i < m; i = i + 2) {
            offers[i] = new Offer(t[i], a[i], b[i]);
            offers[i + 1] = new Offer(t[i], b[i], a[i]);
        }
        long total = counts[n] * n;
        final boolean visited[] = new boolean[counts.length];
        visited[n] = true;
        for (int i = n; i > 0; i--) {
            //try to convert each element to 'i'. Find the path and subtract all those in the path

        }
        return 0;
    }
}

class Offer implements Comparable<Offer> {
    final int time;
    final int give;
    final int take;

    public Offer(int time, int give, int take) {
        this.time = time;
        this.give = give;
        this.take = take;
    }

    @Override
    public int compareTo(Offer other) {
        if (this.take != other.take) {
            return this.take - other.take;
        } else {
            return this.time - other.time;
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
