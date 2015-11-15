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

    public static void main(String[] args) throws IOException {
        InputReaders br = new InputReaders(System.in);
        final int field[][] = new int[5][5];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = br.readInt();
            }
        }
        Board board = new Board(field, order);
        if (!board.findAnyAlmostCompleteSquare()) {
            if (!board.findLeastCompletedSquare()) {
                board.findRandomSquare();
            }
        }
    }


}

class Board {
    private final int[][] board;
    private final List<Integer> order;
    private final int[][] counts;
    final boolean visited[][];

    public Board(int[][] board, List<Integer> order) {
        this.board = board;
        this.order = order;
        counts = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int bits = 0;
                for (int k = 0; k < 4; k++) {
                    if ((board[i][j] & (1 << k)) != 0) {
                        bits++;
                    }
                }
                counts[i][j] = bits;
            }
        }
        visited = new boolean[board.length][board.length];
    }

    public boolean findAnyAlmostCompleteSquare() {
        boolean played = false;
        for (int index = 0; index < order.size() && !played; index++) {
            int x = order.get(index);
            int i = x / 5, j = x % 5;
            if (counts[i][j] == 3) {
                played = true;
                board[i][j] ^= 15;
                for (int k = 0; k < 4; k++) {
                    if ((board[i][j] & (1 << k)) != 0) {
                        System.out.println(i + " " + j + " " + k);
                        break;
                    }
                }
            }
        }
        return played;
    }

    public boolean findLeastCompletedSquare() {
        boolean played = false;
        for (int index = 0; index < order.size() && !played; index++) {
            int x = order.get(index);
            int i = x / 5, j = x % 5;
            if (counts[i][j] == 0 || counts[i][j] == 1) {
                board[i][j] ^= 15;
                int top = i > 0 ? counts[i - 1][j] : 0, bottom = i < board.length - 1 ? counts[i + 1][j] : 0;
                int left = j > 0 ? counts[i][j - 1] : 0, right = j < board.length - 1 ? counts[i][j + 1] : 0;
                if ((board[i][j] & 1) != 0 && top < 2) {
                    System.out.println(i + " " + j + " " + 0);
                    played = true;
                } else if ((board[i][j] & 2) != 0 && right < 2) {
                    System.out.println(i + " " + j + " " + 1);
                    played = true;
                } else if ((board[i][j] & 4) != 0 && bottom < 2) {
                    System.out.println(i + " " + j + " " + 2);
                    played = true;
                } else if ((board[i][j] & 8) != 0 && left < 2) {
                    System.out.println(i + " " + j + " " + 3);
                    played = true;
                }
            }
        }
        return played;
    }

    public boolean findRandomSquare() {
        boolean played = false;
        int size = 0;
        final Point point[] = new Point[25];
        for (int index = 0; index < order.size() && !played; index++) {
            int x = order.get(index);
            int i = x / 5, j = x % 5;
            if (!visited[i][j] && counts[i][j] < 4) {
                played = true;
                board[i][j] ^= 15;
                point[size++] = new Point(i, j, BFS(i, j));
            }
        }
        Arrays.sort(point, 0, size);
        for (int k = 0; k < 4; k++) {
            if ((board[point[0].x][point[0].y] & (1 << k)) != 0) {
                System.out.println(point[0].x + " " + point[0].y + " " + k);
                break;
            }
        }
        return played;
    }

    private int BFS(int i, int j) {
        final int q[] = new int[25];
        int front = 0, rear = 1;
        q[0] = i * board.length + j;
        visited[i][j] = true;
        while (front < rear) {
            int row = q[front] / board.length, col = q[front] % board.length;
            if (row > 0 && counts[row - 1][col] == 2) {
                q[rear++] = (row - 1) * board.length + col;
                visited[i - 1][j] = true;
            }
            if (row < board.length - 1 && counts[row + 1][col] == 2) {
                q[rear++] = (row + 1) * board.length + col;
                visited[i + 1][j] = true;
            }
            if (col > 0 && counts[row][col - 1] == 2) {
                q[rear++] = row * board.length + col - 1;
                visited[i][j - 1] = true;
            }
            if (col < board.length - 1 && counts[row][col + 1] == 2) {
                q[rear++] = row * board.length + col + 1;
                visited[i][j + 1] = true;
            }
        }
        return rear;
    }
}

class Point implements Comparable<Point> {
    final int x, y, area;

    public Point(int x, int y, int area) {
        this.x = x;
        this.y = y;
        this.area = area;
    }

    @Override
    public int compareTo(Point other) {
        return this.area - other.area;
    }
}