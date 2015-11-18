import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DotsAndDashes {

    public static void main(String[] args) throws IOException {
        final InputReaders br = new InputReaders(System.in);
        final int field[][] = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = br.readInt();
            }
        }
    }
}

class Strategy {
    private final int counts[] = new int[25];
    private final int[] board = new int[25];

    public Strategy(final int[][] board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    this.board[i * 5 + j] = board[i][j];
                    if ((board[i][j] & (1 << k)) != 0) {
                        counts[i * 5 + j]++;
                    }
                }
            }
        }
    }

    public Edge solve() {
        int captureBox = getCaptureBox();
        if (captureBox < 0) {
            for (int k = 0; k < 4; k++) {
                if ((board[captureBox] & (1 << k)) != 0) {
                    return new Edge(captureBox / 5, captureBox % 5, k);
                }
            }
        } else {

        }
    }

    public int getCaptureBox() {
        Chain[] allChains = findAllChains();
        if (allChains.length > 1) {
            return allChains[0].start;
        } else if (allChains.length == 1) {
            if (allChains[0].open) {
                if (allChains[0].size != 2) {
                    return allChains[0].start;
                } else {

                }
            } else {
                if (allChains[0].size != 4) {
                    return allChains[0].start;
                } else {

                }
            }
        } else {
            return -1;
        }
    }

    public Chain[] findAllChains() {
        int visited = 0;
        final List<Chain> chains = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if ((visited & (1 << i)) != 0) {
                if (counts[i] == 3) {
                    boolean open = false;
                    final int q[] = new int[25];
                    int front = 0, rear = 1;
                    rear = checkNeighbours(i, q, rear);
                    while (front < rear) {
                        int current = q[front++];
                        if (counts[current] == 2) {
                            rear = checkNeighbours(current, q, rear);
                        } else if (counts[current] < 2) {
                            open = true;
                        }
                    }
                    chains.add(new Chain(i, open, rear));
                }
            }
        }
        Chain[] chainArray = chains.toArray(new Chain[chains.size()]);
        Arrays.sort(chainArray);
        return chainArray;
    }

    private int checkNeighbours(int i, int[] q, int rear) {
        if ((i & 8) == 0 && i % 5 != 0) {
            q[rear++] = i - 1;
        }
        if ((i & 1) == 0 && i / 5 != 0) {
            q[rear++] = i - 5;
        }
        if ((i & 4) == 0 && i / 5 != 4) {
            q[rear++] = i + 5;
        }
        if ((i & 2) == 0 && i % 5 != 4) {
            q[rear++] = i + 1;
        }
        return rear;
    }
}

class Chain implements Comparable<Chain> {
    protected final int start;
    protected final boolean open;
    protected final int size;

    Chain(int start, boolean open, int size) {
        this.start = start;
        this.open = open;
        this.size = size;
    }

    @Override
    public int compareTo(Chain other) {
        if (this.open != other.open) {
            if (this.open) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return other.size - this.size;
        }
    }
}

class Edge {
    private final int x, y, side;

    Edge(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + side;
    }
}
