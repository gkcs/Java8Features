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
        final InputReader reader = new InputReader(System.in);
        final Solver solver = new Solver();
        final StringBuilder stringBuilder = new StringBuilder();
        for (int testcases = reader.readInt(); testcases > 0; testcases--) {
            stringBuilder.append(solver.solve(reader.readString(), reader.readString())).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {

    private int[] failure;

    public int solve(String text, String pattern) {
        failure = new int[pattern.length()];
        failure[0] = -1;
        for (int j = 1; j < pattern.length(); j++) {
            int i = failure[j - 1];
            while ((pattern.charAt(j) != pattern.charAt(i + 1)) && i >= 0)
                i = failure[i];
            if (pattern.charAt(j) == pattern.charAt(i + 1))
                failure[j] = i + 1;
            else
                failure[j] = -1;
        }
        return posMatch(text, pattern);
    }

    private int posMatch(String text, String pat) {
        int i = 0, j = 0;
        while (i < text.length() && j < pat.length()) {
            if (text.charAt(i) == pat.charAt(j)) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = failure[j - 1] + 1;
            }
        }
        return ((j == pat.length()) ? (i - pat.length()) : -1);
    }
}
