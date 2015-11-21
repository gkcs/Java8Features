import java.io.IOException;
import java.io.InputStream;
import java.util.*;


class InputTaker {
    private InputStream stream;
    private byte[] buf = new byte[1024];

    private int curChar;

    private int numChars;

    public InputTaker(InputStream stream) {
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


}


public class DotsAndDashes {

    public static void main(String[] args) throws IOException {
        final InputTaker br = new InputTaker(System.in);
        final int field[][] = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = br.readInt();
            }
        }
        br.readInt();
        System.out.println(new Strategy(5).input(field));
    }
}

class Strategy {

    private final int size;
    private final int boundary;
    private final int boardSize;
    private int visited = 0;

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
        return getCaptureBox(board, sides, counts);
    }

    private Edge getCaptureBox(final byte[] board, final int counts[], int[] numberOfSides) {
        List<Chain> allChains = findAllChains(board, counts);
        if (allChains.size() > 1) {
            return getEdge(allChains.get(0).start, allChains.get(0).side);
        } else if (allChains.size() == 1) {
            if (allChains.get(0).open) {
                if (allChains.get(0).size != 2) {
                    return getEdge(allChains.get(0).start, allChains.get(0).side);
                } else {
                    if (findAnyEmptyBox(board, numberOfSides) != null) {
                        return getEdge(allChains.get(0).start, allChains.get(0).side);
                    } else {
                        List<Chain> allDoubleEndedChains = findAllDoubleEndedChains(board, counts);
                        if (allDoubleEndedChains.size() > 0 && (allDoubleEndedChains.size() & 1) != 0 && allDoubleEndedChains.get(0).size > 2) {
                            return findEndOfChain(board, allChains.get(0).start);
                        } else {
                            return getEdge(allChains.get(0).start, allChains.get(0).side);
                        }
                    }
                }
            } else {
                if (allChains.get(0).size != 4) {
                    return getEdge(allChains.get(0).start, allChains.get(0).side);
                } else {
                    if (findAnyEmptyBox(board, numberOfSides) != null) {
                        return getEdge(allChains.get(0).start, allChains.get(0).side);
                    } else {
                        List<Chain> allDoubleEndedChains = findAllDoubleEndedChains(board, counts);
                        if (allDoubleEndedChains.size() > 0 && (allDoubleEndedChains.size() & 1) != 0 && allDoubleEndedChains.get(0).size > 4) {
                            return findEndOfChain(board, allChains.get(0).start);
                        } else {
                            return getEdge(allChains.get(0).start, allChains.get(0).side);
                        }
                    }
                }
            }
        } else {
            Edge emptyBox = findAnyEmptyBox(board, numberOfSides);
            if (emptyBox != null) {
                return emptyBox;
            } else {
                List<Chain> allDoubleEndedChains = findAllDoubleEndedChains(board, counts);
                if (allDoubleEndedChains.size() > 0) {
                    return getEdge(allDoubleEndedChains.get(0).start, allDoubleEndedChains.get(0).side);
                } else {
                    return anyPossibleMove(counts, board);
                }
            }
        }
    }

    private List<Chain> findAllDoubleEndedChains(byte[] board, int[] counts) {
        final List<Chain> chains = new ArrayList<Chain>(5);
        for (int i = 0; i < boardSize; i++) {
            if ((visited & (1 << i)) == 0 && (counts[2] & (1 << i)) != 0) {
                final int q[] = new int[boardSize];
                int front = 0, rear = 0;
                visited |= (1 << i);
                int side = 0;
                if ((board[i] & 8) == 0 && i % size != 0 && (visited & (1 << (i - 1))) == 0) {
                    q[rear++] = i - 1;
                    side = 3;
                    visited |= (1 << (i - 1));
                }
                if ((board[i] & 1) == 0 && i / size != 0 && (visited & (1 << (i - size))) == 0) {
                    q[rear++] = i - size;
                    side = 0;
                    visited |= (1 << (i - size));
                }
                if ((board[i] & 4) == 0 && i / size != boundary && (visited & (1 << (i + size))) == 0) {
                    q[rear++] = i + size;
                    side = 2;
                    visited |= (1 << (i + size));
                }
                if ((board[i] & 2) == 0 && i % size != boundary && (visited & (1 << (i + 1))) == 0) {
                    q[rear++] = i + 1;
                    side = 1;
                    visited |= (1 << (i + 1));
                }
                while (front < rear) {
                    int current = q[front++];
                    if ((counts[2] & (1 << current)) != 0) {
                        if ((board[current] & 8) == 0 && current % size != 0 && (visited & (1 << (current - 1))) == 0) {
                            q[rear++] = current - 1;
                            visited |= (1 << (current - 1));
                        }
                        if ((board[current] & 1) == 0 && current / size != 0 && (visited & (1 << (current - size))) == 0) {
                            q[rear++] = current - size;
                            visited |= (1 << (current - size));
                        }
                        if ((board[current] & 4) == 0 && current / size != boundary && (visited & (1 << (current + size))) == 0) {
                            q[rear++] = current + size;
                            visited |= (1 << (current + size));
                        }
                        if ((board[current] & 2) == 0 && current % size != boundary && (visited & (1 << (current + 1))) == 0) {
                            q[rear++] = current + 1;
                            visited |= (1 << (current + 1));
                        }
                    }
                }
                chains.add(new Chain(i, side, true, rear + 1));
            }
        }
        Collections.sort(chains, new Comparator<Chain>() {
            @Override
            public int compare(Chain first, Chain second) {
                if (first.open != second.open) {
                    if (first.open) {
                        return +1;
                    } else {
                        return -1;
                    }
                } else {
                    return second.size - first.size;
                }
            }
        });
        return chains;
    }

    private Edge anyPossibleMove(int[] counts, byte[] board) {
        int i;
        for (i = 0; i < boardSize; i++) {
            if ((counts[4] & (1 << i)) == 0) {
                break;
            }
        }
        return getEdge(i, findSide(i, board));
    }

    private int findSide(final int position, final byte board[]) {
        int bit = -1;
        for (int k = 0; k < 4; k++) {
            if ((board[position] & (1 << k)) == 0) {
                bit = k;
                break;
            }
        }
        if (bit >= 0) {
            return bit;
        } else {
            throw new IllegalStateException("No side available");
        }
    }

    private Edge findAnyEmptyBox(byte[] board, int[] counts) {
        for (int i = 0; i < boardSize; i++) {
            if ((counts[i] < 2)) {
                boolean left = (((i % size) == 0) || (counts[i - 1] < 2)) && ((board[i] & 8) == 0);
                boolean right = (((i % size) == boundary) || (counts[i + 1] < 2)) && ((board[i] & 2) == 0);
                boolean down = (((i / size) == boundary) || (counts[i + size] < 2)) && ((board[i] & 4) == 0);
                boolean top = (((i / size) == 0) || (counts[i - size] < 2)) && ((board[i] & 1) == 0);
                if (top) {
                    return getEdge(i, 0);
                } else if (down) {
                    return getEdge(i, 2);
                } else if (right) {
                    return getEdge(i, 1);
                } else if (left) {
                    return getEdge(i, 3);
                }
            }
        }
        return null;
    }

    private Edge findEndOfChain(final byte board[], final int i) {
        if ((board[i] & 8) == 0) {
            return getEdge(i - 1, 3);
        } else if ((board[i] & 1) == 0) {
            return getEdge(i - size, 0);
        } else if ((board[i] & 4) == 0) {
            return getEdge(i + size, 2);
        } else if ((board[i] & 2) == 0) {
            return getEdge(i + 1, 1);
        }
        throw new IllegalStateException("Should have an open side");
    }

    private Edge getEdge(int i, int k) {
        return new Edge(i / size, i % size, k);
    }


    private List<Chain> findAllChains(final byte[] board, final int counts[]) {
        final List<Chain> chains = new ArrayList<Chain>(5);
        for (int i = 0; i < boardSize; i++) {
            if ((visited & (1 << i)) == 0 && (counts[3] & (1 << i)) != 0) {
                boolean open = false;
                final int q[] = new int[boardSize];
                int front = 0, rear = 0, excess = 0;
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
                        excess++;
                    }
                }
                chains.add(new Chain(i, findSide(i, board), open, rear + 1 - excess));
            }
        }
        Collections.sort(chains, new Comparator<Chain>() {
            @Override
            public int compare(Chain first, Chain second) {
                if (first.open != second.open) {
                    if (first.open) {
                        return +1;
                    } else {
                        return -1;
                    }
                } else {
                    return second.size - first.size;
                }
            }
        });
        return chains;
    }

}

class Chain {
    protected final int start;
    protected final int side;
    protected final boolean open;
    protected final int size;

    Chain(int start, int side, boolean open, int size) {
        this.start = start;
        this.side = side;
        this.open = open;
        this.size = size;
    }
}

class Edge {
    protected final int x, y, side;

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