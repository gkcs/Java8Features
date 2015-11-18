import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DotsAndDashes {

    public static void main(String[] args) throws IOException {
        final InputReaders br = new InputReaders(System.in);
        final Strategy strategy = new Strategy();
        final int field[][] = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = br.readInt();
            }
        }
        final byte board[] = new byte[25];
        final int counts[] = new int[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int current = i * 5 + j;
                for (int k = 0; k < 4; k++) {
                    board[current] = (byte) field[i][j];
                    if ((field[i][j] & (1 << k)) != 0) {
                        counts[current]++;
                    }
                }
            }
        }
        final int sides[] = new int[5];
        for (int i = 0; i < 25; i++) {
            sides[counts[i]] |= 1 << i;
        }
        System.out.println(strategy.solve(board, sides));
    }
}

class Strategy {

    private static final int five = 5;
    private static final int four = 4;

    public Edge solve(final byte[] board, final int[] sides) {
        int captureBox = getCaptureBox(board, sides);
        if (captureBox >= 0) {
            for (int k = 0; k < four; k++) {
                if ((board[captureBox] & (1 << k)) != 0) {
                    return new Edge(captureBox / five, captureBox % five, k);
                }
            }
        } else {
            return null;
        }
        return null;
    }

    private int getCaptureBox(final byte[] board, final int sides[]) {
        List<Chain> allChains = findAllChains(board, sides);
        if (allChains.size() > 1) {
            return allChains.get(0).start;
        } else if (allChains.size() == 1) {
            if (allChains.get(0).open) {
                if (allChains.get(0).size != 2) {
                    return allChains.get(0).start;
                } else {
                    return allChains.get(0).start;
                }
            } else {
                if (allChains.get(0).size != four) {
                    return allChains.get(0).start;
                } else {
                    return allChains.get(0).start;
                }
            }
        } else {
            return -1;
        }
    }

    private List<Chain> findAllChains(final byte[] board, final int sides[]) {
        int visited = 0;
        final List<Chain> chains = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if ((visited & (1 << i)) != 0) {
                if ((sides[3] & (1 << i)) != 0) {
                    boolean open = false;
                    final int q[] = new int[25];
                    int front = 0, rear = 1;
                    if ((board[i] & 8) == 0 && i % five != 0 && ((visited & (1 << (i - 1))) == 0)) {
                        q[rear++] = i - 1;
                        visited |= (1 << (i - 1));
                    }
                    if ((board[i] & 1) == 0 && i / five != 0 && ((visited & (1 << (i - five))) == 0)) {
                        q[rear++] = i - five;
                        visited |= (1 << (i - five));
                    }
                    if ((board[i] & four) == 0 && i / five != four && ((visited & (1 << (i + five))) == 0)) {
                        q[rear++] = i + five;
                        visited |= (1 << (i + five));
                    }
                    if ((board[i] & 2) == 0 && i % five != four && ((visited & (1 << (i + 1))) == 0)) {
                        q[rear++] = i + 1;
                        visited |= (1 << (i + 1));
                    }
                    while (front < rear) {
                        int current = q[front++];
                        if ((sides[2] & (1 << current)) != 0) {
                            if ((board[current] & 8) == 0 && current % five != 0 && ((visited & (1 << (current - 1))) == 0)) {
                                q[rear++] = current - 1;
                                visited |= (1 << (current - 1));
                            }
                            if ((board[current] & 1) == 0 && current / five != 0 && ((visited & (1 << (current - five))) == 0)) {
                                q[rear++] = current - five;
                                visited |= (1 << (current - five));
                            }
                            if ((board[current] & four) == 0 && current / five != four && ((visited & (1 << (current + five))) == 0)) {
                                q[rear++] = current + five;
                                visited |= (1 << (current + five));
                            }
                            if ((board[current] & 2) == 0 && current % five != four && ((visited & (1 << (current + 1))) == 0)) {
                                q[rear++] = current + 1;
                                visited |= (1 << (current + 1));
                            }
                        } else if ((sides[0] & (1 << current)) != 0 || (sides[1] & (1 << current)) != 0) {
                            open = true;
                        }
                    }
                    chains.add(new Chain(i, open, rear));
                }
            }
        }
        chains.sort((first, second) -> {
            if (first.open != second.open) {
                if (first.open) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                return second.size - first.size;
            }
        });
        return chains;
    }

}

class Chain {
    protected final int start;
    protected final boolean open;
    protected final int size;

    Chain(int start, boolean open, int size) {
        this.start = start;
        this.open = open;
        this.size = size;
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