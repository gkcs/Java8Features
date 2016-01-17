package hackerEarth;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    public static final int FIVE = 5;

    public static void main(String[] args) throws IOException {
        final InputReaders br = new InputReaders(System.in);
        final int field[][] = new int[FIVE][FIVE];
        final List<Integer> order = new ArrayList<Integer>();
        for (int i = 0; i < 25; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        for (int i = 0; i < FIVE; i++) {
            for (int j = 0; j < FIVE; j++) {
                field[i][j] = br.readInt();
            }
        }
        final Board board = new Board(field, order);
        if (!board.findAnyAlmostCompleteSquare() || board.move == null) {
            if (!board.findLeastCompletedSquare() || board.move == null) {
                board.findRandomSquare();
            }
        }
        System.out.println(board.move);
    }
}

class Board {
    public static final int five = 5;
    private final Random random = new Random();
    private final int[][] board;
    private final Point candidates[] = new Point[4];

    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }

    private final int[] order;
    private final int[][] counts;
    private final boolean visited[][];
    public Point move;

    public Board(final int[][] board, final List<Integer> order) {
        this.board = board;
        this.order = new int[order.size()];
        for (int i = 0; i < this.order.length; i++) {
            this.order[i] = order.get(i);
        }
        counts = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                for (int k = 0; k < 4; k++) {
                    if ((board[i][j] & (1 << k)) != 0) {
                        counts[i][j]++;
                    }
                }
            }
        }
        visited = new boolean[board.length][board.length];
    }

    public boolean findAnyAlmostCompleteSquare() {
        boolean played = false;
        for (int index = 0; index < order.length && !played; index++) {
            final int i = order[index] / five, j = order[index] % five;
            if (counts[i][j] == 3) {
                int bits = board[i][j] ^ 15;
                for (int k = 0; k < 4; k++) {
                    if ((bits & (1 << k)) != 0) {
                        move = new Point(i, j, 0, k);
                        played = true;
                        break;
                    }
                }
            }
        }
        return played;
    }

    public boolean findLeastCompletedSquare() {
        boolean played = false;
        for (int index = 0; index < order.length && !played; index++) {
            final int i = order[index] / five, j = order[index] % five;
            if (counts[i][j] == 0 || counts[i][j] == 1) {
                int bits = board[i][j] ^ 15;
                final int top = i > 0 ? counts[i - 1][j] : 0, bottom = i < five - 1 ? counts[i + 1][j] : 0;
                final int left = j > 0 ? counts[i][j - 1] : 0, right = j < five - 1 ? counts[i][j + 1] : 0;
                int ppl = 0;
                if ((bits & 1) != 0 && top < 2) {
                    candidates[ppl] = new Point(i, j, 0, 0);
                    ppl++;
                }
                if ((bits & 2) != 0 && right < 2) {
                    candidates[ppl] = new Point(i, j, 0, 1);
                    ppl++;
                }
                if ((bits & 4) != 0 && bottom < 2) {
                    candidates[ppl] = new Point(i, j, 0, 2);
                    ppl++;
                }
                if ((bits & 8) != 0 && left < 2) {
                    candidates[ppl] = new Point(i, j, 0, 3);
                    ppl++;
                }
                if (ppl > 0) {
                    move = candidates[random.nextInt(ppl)];
                    played = true;
                    break;
                }
            }
        }
        return played;
    }

    public void findRandomSquare() {
        int size = 0;
        final Point point[] = new Point[25];
        for (int x : order) {
            int i = x / five, j = x % five;
            if (!visited[i][j] && counts[i][j] < 4) {
                point[size++] = new Point(i, j, BFS(i, j), 0);
            }
        }
        Arrays.sort(point, 0, size);
        int bits = board[point[0].x][point[0].y] ^ 15;
        for (int k = 0; k < 4; k++) {
            if ((bits & (1 << k)) != 0) {
                move = new Point(point[0].x, point[0].y, 0, k);
                break;
            }
        }
    }

    private int BFS(int i, int j) {
        final int q[] = new int[25];
        int front = 0, rear = 1;
        q[0] = i * five + j;
        visited[i][j] = true;
        while (front < rear) {
            final int row = q[front] / five, col = q[front] % five;
            int bits = board[row][col] ^ 15;
            if (row > 0 && counts[row - 1][col] == 2 && !visited[row - 1][col] && (bits & 1) != 0) {
                q[rear++] = (row - 1) * five + col;
                visited[row - 1][col] = true;
            }
            if (row < five - 1 && counts[row + 1][col] == 2 && !visited[row + 1][col] && (bits & 4) != 0) {
                q[rear++] = (row + 1) * five + col;
                visited[row + 1][col] = true;
            }
            if (col > 0 && counts[row][col - 1] == 2 && !visited[row][col - 1] && (bits & 8) != 0) {
                q[rear++] = row * five + col - 1;
                visited[row][col - 1] = true;
            }
            if (col < five - 1 && counts[row][col + 1] == 2 && !visited[row][col + 1] && (bits & 2) != 0) {
                q[rear++] = row * five + col + 1;
                visited[row][col + 1] = true;
            }
            front++;
        }
        return rear;
    }
}

class Point extends Edge implements Comparable<Point> {
    final int area;

    public Point(int x, int y, int area, int side) {
        super(x, y, side);
        this.area = area;
    }

    @Override
    public int compareTo(Point other) {
        return this.area - other.area;
    }
}