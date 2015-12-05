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
            int[] a = new int[n], b = new int[n], c = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = br.readInt();
                b[i] = br.readInt();
                c[i] = br.readInt();
            }
            stringBuilder.append(solver.solve(n, a, b, c)).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {
    public int solve(int n, int[] a, int[] b, int[] c) {
        Line[] lines = new Line[n];
        for (int i = 0; i < lines.length; i++) {
            final int common = gcd(a[i], b[i]);
            final int coeff = gcd(common, c[i]);
            lines[i] = new Line(a[i] / common, b[i] / common, c[i] / coeff, common / coeff);
        }
        Arrays.sort(lines);
        int largest = 1, current = 1;
        for (int i = 1; i < n; i++) {
            if (lines[i].hasSameSlope(lines[i - 1])) {
                if (!lines[i].equals(lines[i - 1])) {
                    current++;
                }
            } else {
                if (largest < current) {
                    largest = current;
                }
                current = 1;
            }
        }
        if (largest < current) {
            largest = current;
        }
        return largest;
    }

    private int gcd(int a, int b) {
        if (a < b) {
            final int temp = a;
            a = b;
            b = temp;
        }
        while (b > 0) {
            final int temp = b;
            b = a % b;
            a = temp;
        }
        return a == 0 ? 1 : a;
    }
}

class Line implements Comparable<Line> {
    final int x, y, num, div;

    @Override
    public String toString() {
        return "Line{" +
                "x=" + x +
                ", y=" + y +
                ", num=" + num +
                ", div=" + div +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        Line line = (Line) o;
        return x == line.x && y == line.y && num == line.num && div == line.div;

    }

    Line(int x, int y, int num, int div) {
        this.x = x;
        this.y = y;

        this.num = num;
        this.div = div;
    }

    @Override
    public int compareTo(Line other) {
        if (x != other.x) {
            return x - other.x;
        } else {
            if (y != other.y) {
                return y - other.y;
            } else {
                if (num == other.num && div == other.div) {
                    return 0;
                } else {
                    if (num / (double) div < other.num / (double) other.div) {
                        return -1;
                    } else {
                        return +1;
                    }
                }
            }
        }
    }

    public boolean hasSameSlope(Line line) {
        return x == line.x && y == line.y;
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


