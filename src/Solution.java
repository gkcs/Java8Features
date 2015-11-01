import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;

class InputReaders {
    private InputStream stream;
    private byte[] buf = new byte[1024];

    private int curChar;

    private int numChars;

    public InputReaders(InputStream stream) {
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

public class Solution {

    public static void main(String[] args) throws IOException {
        InputReaders br = new InputReaders(System.in);
        final int n = br.readInt();
        final int[] sides = new int[n], frequencies = new int[10001];
        for (int i = 0; i < n; i++) {
            sides[i] = br.readInt();
            frequencies[sides[i]]++;
        }
        int acute = 0, right = 0, obtuse = 0;
        for (int i = 0; i < n; i++) {
            int largest = sides[n - 1 - i];
            for (int j = 0; j < n; j++) {
                int smallest = sides[j];
                int lowerRange = largest - smallest + 1;
                int start = Arrays.binarySearch(sides, j, n - i, lowerRange);
                if (start < 0) {
                    start = -start - 1;
                } else {
                    while (start > 0 && sides[start - 1] == lowerRange) {
                        start--;
                    }
                }
                int rightAngleSide = (int) Math.sqrt(largest * largest - smallest * smallest);
                if (rightAngleSide * rightAngleSide + smallest * smallest == largest * largest) {
                    int mid = Arrays.binarySearch(sides, start, n - i, rightAngleSide);
                    if(mid<0){
                        right=n-i-mid;
                    }
                    else{

                    }
                } else {
                    int mid = Arrays.binarySearch(sides, start, n - i, rightAngleSide);
                }
            }
        }
        System.out.println(acute + " " + right + " " + obtuse);
    }
}