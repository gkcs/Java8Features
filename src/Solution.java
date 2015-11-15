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
        if (!board.findAnyAlmostCompleteSquare()) {
            if (!board.findLeastCompletedSquare()) {
                board.findRandomSquare();
            }
        }
        System.out.println(board.move);
    }


}

class Board {
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
            int i = order[index] / 5, j = order[index] % 5;
            if (counts[i][j] == 3) {
                played = true;
                for (int k = 0; k < 4; k++) {
                    if ((board[i][j] & (1 << k)) == 0) {
                        move = new Point(i, j, 0, k);
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
            int x = order[index];
            int i = x / 5, j = x % 5;
            if (counts[i][j] == 0 || counts[i][j] == 1) {
                final int top = i > 0 ? counts[i - 1][j] : 0, bottom = i < board.length - 1 ? counts[i + 1][j] : 0;
                final int left = j > 0 ? counts[i][j - 1] : 0, right = j < board.length - 1 ? counts[i][j + 1] : 0;
                int ppl = 0;
                if ((board[i][j] & 1) == 0 && top < 2) {
                    candidates[ppl++] = new Point(i, j, 0, 0);
                }
                if ((board[i][j] & 2) == 0 && right < 2) {
                    candidates[ppl++] = new Point(i, j, 0, 1);
                }
                if ((board[i][j] & 4) == 0 && bottom < 2) {
                    candidates[ppl++] = new Point(i, j, 0, 2);
                }
                if ((board[i][j] & 8) == 0 && left < 2) {
                    candidates[ppl++] = new Point(i, j, 0, 3);
                }
                if (ppl > 0) {
                    played = true;
                    move = candidates[random.nextInt(ppl)];
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
            int i = x / 5, j = x % 5;
            if (!visited[i][j] && counts[i][j] < 4) {
                point[size++] = new Point(i, j, BFS(i, j), 0);
            }
        }
        Arrays.sort(point, 0, size);
        for (int k = 0; k < 4; k++) {
            if ((board[point[0].x][point[0].y] & (1 << k)) == 0) {
                move = new Point(point[0].x, point[0].y, 0, k);
                break;
            }
        }
    }

    private int BFS(int i, int j) {
        final int q[] = new int[25];
        int front = 0, rear = 1;
        q[0] = i * board.length + j;
        visited[i][j] = true;
        while (front < rear) {
            int row = q[front] / board.length, col = q[front] % board.length;
            if (row > 0 && counts[row - 1][col] == 2 && !visited[row - 1][col] && (board[row][col] & 1) == 0) {
                q[rear++] = (row - 1) * board.length + col;
                visited[row - 1][col] = true;
            }
            if (row < board.length - 1 && counts[row + 1][col] == 2 && !visited[row + 1][col] && (board[row][col] & 4) == 0) {
                q[rear++] = (row + 1) * board.length + col;
                visited[row + 1][col] = true;
            }
            if (col > 0 && counts[row][col - 1] == 2 && !visited[row][col - 1] && (board[row][col] & 8) == 0) {
                q[rear++] = row * board.length + col - 1;
                visited[row][col - 1] = true;
            }
            if (col < board.length - 1 && counts[row][col + 1] == 2 && !visited[row][col + 1] && (board[row][col] & 2) == 0) {
                q[rear++] = row * board.length + col + 1;
                visited[row][col + 1] = true;
            }
            front++;
        }
        return rear;
    }
}

class Point implements Comparable<Point> {
    final int x, y, area, side;

    @Override
    public int hashCode() {
        return x + y + area + side;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Point) {
            Point point = (Point) obj;
            return x == point.x && y == point.y && side == point.side;
        } else return false;
    }

    public Point(int x, int y, int area, int side) {
        this.x = x;
        this.y = y;
        this.area = area;
        this.side = side;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + side;
    }

    @Override
    public int compareTo(Point other) {
        return this.area - other.area;
    }
}