import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DotsAndDashes {

    public static void main(String[] args) throws IOException {
        final InputReaders br = new InputReaders(System.in);
        final Strategy strategy = new Strategy(5);
        final int field[][] = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = br.readInt();
            }
        }
        System.out.println(strategy.input(field));
    }
}

class Strategy {

    private final int size;
    private final int boundary;
    private final int boardSize;

    Strategy(int size) {
        this.size = size;
        boardSize = size * size;
        boundary = size - 1;
    }

    public Edge input(int field[][]) {
        final byte board[] = new byte[boardSize];
        final int counts[] = new int[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int current = i * size + j;
                for (int k = 0; k < 4; k++) {
                    board[current] = (byte) field[i][j];
                    if ((field[i][j] & (1 << k)) != 0) {
                        counts[current]++;
                    }
                }
            }
        }
        final int sides[] = new int[5];
        for (int i = 0; i < size * size; i++) {
            sides[counts[i]] |= 1 << i;
        }
        return solve(board, sides);
    }

    private Edge solve(final byte[] board, final int[] counts) {
        int captureBox = getCaptureBox(board, counts);
        if (captureBox >= 0) {
            for (int k = 0; k < boundary; k++) {
                if ((board[captureBox] & (1 << k)) != 0) {
                    return new Edge(captureBox / size, captureBox % size, k);
                }
            }
        } else {
            return null;
        }
        return null;
    }

    private int getCaptureBox(final byte[] board, final int counts[]) {
        List<Chain> allChains = findAllChains(board, counts);
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
                if (allChains.get(0).size != 4) {
                    return allChains.get(0).start;
                } else {
                    return allChains.get(0).start;
                }
            }
        } else {
            return -1;
        }
    }


    private List<Chain> findAllChains(final byte[] board, final int counts[]) {
        int visited = 0;

        final List<Chain> chains = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            if ((visited & (1 << i)) == 0 && (counts[3] & (1 << i)) != 0) {
                boolean open = false;
                final int q[] = new int[boardSize];
                int front = 0, rear = 0;
                visited |= (1 << i);
                if ((board[i] & 8) == 0 && i % size != 0 && (visited & (1 << (i - 1))) == 0) {
                    q[rear++] = i - 1;
                    visited |= (1 << (i - 1));
                }
                if ((board[i] & 1) == 0 && i / size != 0 && (visited & (1 << (i - size))) == 0) {
                    q[rear++] = i - size;
                    visited |= (1 << (i - size));
                }
                if ((board[i] & 4) == 0 && i / size != boundary && (visited & (1 << (i + size))) == 0) {
                    q[rear++] = i + size;
                    visited |= (1 << (i + size));
                }
                if ((board[i] & 2) == 0 && i % size != boundary && (visited & (1 << (i + 1))) == 0) {
                    q[rear++] = i + 1;
                    visited |= (1 << (i + 1));
                }
                while (front < rear) {
                    int current = q[front++];
                    if ((counts[2] & (1 << current)) != 0) {
                        int internal = 0;
                        if ((board[current] & 8) == 0 && current % size != 0 && (visited & (1 << (current - 1))) == 0) {
                            q[rear++] = current - 1;
                            visited |= (1 << (current - 1));
                            internal++;
                        }
                        if ((board[current] & 1) == 0 && current / size != 0 && (visited & (1 << (current - size))) == 0) {
                            q[rear++] = current - size;
                            visited |= (1 << (current - size));
                            internal++;
                        }
                        if ((board[current] & 4) == 0 && current / size != boundary && (visited & (1 << (current + size))) == 0) {
                            q[rear++] = current + size;
                            visited |= (1 << (current + size));
                            internal++;
                        }
                        if ((board[current] & 2) == 0 && current % size != boundary && (visited & (1 << (current + 1))) == 0) {
                            q[rear++] = current + 1;
                            visited |= (1 << (current + 1));
                            internal++;
                        }
                        if (internal == 0) {
                            open = true;
                        }
                    } else if ((counts[0] & (1 << current)) != 0 || (counts[1] & (1 << current)) != 0) {
                        open = true;
                    }
                }
                chains.add(new Chain(i, open, rear + 1));
            }
        }
        chains.sort((first, second) -> {
            if (first.open != second.open) {
                if (first.open) {
                    return +1;
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

final class GameState {
    final int board[];
    final int counts[];
    final int a;
    final int b;
    final boolean firstPlayerMove;

    GameState(int[] board, int[] counts, int a, int b, boolean firstPlayerMove) {
        this.board = board;
        this.counts = counts;
        this.a = a;
        this.b = b;
        this.firstPlayerMove = firstPlayerMove;
    }
}