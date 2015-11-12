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
            final int n = br.readInt(), m = br.readInt();
            final int cards[] = new int[n];
            for (int i = 0; i < n; i++) {
                cards[i] = br.readInt();
            }
            final int[] t = new int[m], a = new int[m], b = new int[m];
            for (int i = 0; i < m; i++) {
                t[i] = br.readInt();
                a[i] = br.readInt();
                b[i] = br.readInt();
            }
            stringBuilder.append(solver.solve(cards, t, a, b)).append('\n');
        }
        System.out.println(stringBuilder);
    }
}

class Solver {
    public static final int MAX = 50001;
    private final int queue[] = new int[MAX];
    private final int time[] = new int[queue.length];

    public long solve(final int[] cards, final int[] t, final int[] a, final int[] b) {
        final int counts[] = new int[MAX];
        for (int card : cards) {
            counts[card]++;
        }
        final Offer[][] exchanges = getOffers(counts.length, t.length, t, a, b);
        long total = 0;
        final boolean visited[] = new boolean[counts.length];
        for (int i = counts.length - 1; i > 0; i--) {
            total += i * (long) counts[i];
            counts[i] = 0;
            visited[i] = true;
            for (int reachableCity : getReachableCities(i, visited, exchanges)) {
                total += i * (long) counts[reachableCity];
                visited[reachableCity] = true;
                counts[reachableCity] = 0;
            }
        }
        return total;
    }

    private int[] getReachableCities(final int destination, final boolean[] visited, final Offer[][] offers) {
        int front = 0, rear = 1;
        queue[0] = destination;
        time[0] = MAX;
        while (front < rear) {
            int current = queue[front];
            visited[current] = true;
            for (int i = 0; i < offers[current].length; i++) {
                if (!visited[offers[current][i].second] && time[front] >= offers[current][i].time) {
                    queue[rear] = offers[current][i].second;
                    time[rear] = offers[current][i].time;
                    rear++;
                }
            }
            front++;
        }
        final int reachableCities[] = new int[rear];
        System.arraycopy(queue, 0, reachableCities, 0, reachableCities.length);
//        if (destination <= 5) {
//            System.out.println("DESTINATION: " + destination);
//            System.out.println(Arrays.toString(reachableCities));
//        }
        return reachableCities;
    }

    private Offer[][] getOffers(int n, int m, int[] t, int[] a, int[] b) {
        final Offer[][] exchanges = new Offer[n][];
        final int offerCount[] = new int[n];
        final Offer[] offers = new Offer[m << 1];
        for (int i = 0; i < m; i++) {
            offers[i << 1] = new Offer(t[i], a[i], b[i]);
            offers[(i << 1) + 1] = new Offer(t[i], b[i], a[i]);
            offerCount[a[i]]++;
            offerCount[b[i]]++;
        }
        Arrays.parallelSort(offers);
        int start = 0;
        for (int i = 0; i < n; i++) {
            exchanges[i] = new Offer[offerCount[i]];
            System.arraycopy(offers, start, exchanges[i], 0, exchanges[i].length);
            start = start + exchanges[i].length;
        }
        return exchanges;
    }
}

class Offer implements Comparable<Offer> {
    final int time;
    final int first;
    final int second;

    public Offer(int time, int first, int second) {
        this.time = time;
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Offer other) {
        if (this.first != other.first) {
            return this.first - other.first;
        } else if (this.time != other.time) {
            return this.time - other.time;
        } else {
            return this.second - other.second;
        }
    }

    @Override
    public String toString() {
        return "Offer{" +
                "time=" + time +
                ", first=" + first +
                ", second=" + second +
                '}';
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
