package hackerEarth;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Comparator;


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
        try {
            final InputTaker br = new InputTaker(System.in);
            final int field[][] = new int[5][5];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    field[i][j] = br.readInt();
                }
            }
            int player = br.readInt();
            Edge result = new Strategy(5, player == 0).input(field);
            if (result != null) {
                System.out.println(result.x + " " + result.y + " " + result.side);
            } else {
                System.out.println("0 0 0");
            }
        } catch (Exception e) {
            System.out.println("1 1 1");
        }
    }

}

class Strategy {

    private final int size;
    private final int boundary;
    private final int boardSize;
    private int visited = 0;
    private final boolean randomize;
    private static final Random random = new Random();
    private final int[] numberOfSides, counts;
    private final byte board[];

    private final List<Integer> order = new ArrayList<Integer>();

    protected Strategy(int size, boolean randomize) {
        this.size = size;
        this.randomize = randomize;
        this.boardSize = size * size;
        this.boundary = size - 1;
        this.numberOfSides = new int[size * size];
        this.counts = new int[5];
        this.board = new byte[boardSize];
    }

    public Edge input(int field[][]) {
        for (int i = 0; i < boardSize; i++) {
            order.add(i);
        }
        if (randomize) {
            Collections.shuffle(order);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int current = i * size + j;
                board[current] = (byte) (field[i][j] & 15);
                for (int k = 0; k < 4; k++) {
                    if ((field[i][j] & (1 << k)) != 0) {
                        numberOfSides[current]++;
                    }
                }
            }
        }
        for (int i = 0; i < size * size; i++) {
            counts[numberOfSides[i]] |= 1 << i;
        }
        return getCaptureBox();
    }

    private Edge getCaptureBox() {
        List<Chain> allChains = findAllChains();
        if (allChains.size() > 1) {
            return getEdge(allChains.get(0).start, allChains.get(0).side);
        } else if (allChains.size() == 1) {
            if (allChains.get(0).open) {
                if (allChains.get(0).size != 2) {
                    return getEdge(allChains.get(0).start, allChains.get(0).side);
                } else {
                    if (findAnyEmptyBox() != null) {
                        return getEdge(allChains.get(0).start, allChains.get(0).side);
                    } else {
                        List<Chain> allDoubleEndedChains = findAllDoubleEndedChains();
                        if (allDoubleEndedChains.size() > 0 && allDoubleEndedChains.get(0).size > 2) {
                            return findEndOfChain(allChains.get(0));
                        } else {
                            return getEdge(allChains.get(0).start, allChains.get(0).side);
                        }
                    }
                }
            } else {
                if (allChains.get(0).size != 4) {
                    return getEdge(allChains.get(0).start, allChains.get(0).side);
                } else {
                    if (findAnyEmptyBox() != null) {
                        return getEdge(allChains.get(0).start, allChains.get(0).side);
                    } else {
                        List<Chain> allDoubleEndedChains = findAllDoubleEndedChains();
                        if (allDoubleEndedChains.size() > 0 && allDoubleEndedChains.get(0).size > 4) {
                            return findEndOfChain(allChains.get(0));
                        } else {
                            return getEdge(allChains.get(0).start, allChains.get(0).side);
                        }
                    }
                }
            }
        } else {
            Edge emptyBox = findAnyEmptyBox();
            if (emptyBox != null) {
                return emptyBox;
            } else {
                List<Chain> allDoubleEndedChains = findAllDoubleEndedChains();
                if (allDoubleEndedChains.size() > 0) {
                    return getEdge(allDoubleEndedChains.get(0).start, allDoubleEndedChains.get(0).side);
                } else {
                    return anyPossibleMove();
                }
            }
        }
    }

    private List<Chain> findAllDoubleEndedChains() {
        final List<Chain> chains = new ArrayList<Chain>(5);
        for (int index = 0; index < boardSize; index++) {
            final int i = order.get(index);
            if ((visited & (1 << i)) == 0 && (counts[2] & (1 << i)) != 0) {
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
                    } else if ((counts[0] & (1 << current)) != 0 || (counts[1] & (1 << current)) != 0) {
                        excess++;
                    }
                }
                chains.add(new Chain(i, findSide(i), true, rear + 1 - excess));
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
                    return first.size - second.size;
                }
            }
        });
        return chains;
    }

    private Edge anyPossibleMove() {
        int i;
        for (i = 0; i < boardSize; i++) {
            if ((counts[4] & (1 << i)) == 0) {
                break;
            }
        }
        return getEdge(i, findSide(i));
    }

    private int findSide(final int position) {
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

    private Edge findAnyEmptyBox() {
        for (int index = 0; index < boardSize; index++) {
            final int position = order.get(index);
            if ((numberOfSides[position] < 2)) {
                boolean left = (((position % size) == 0) || (numberOfSides[position - 1] < 2)) && ((board[position] & 8) == 0);
                boolean right = (((position % size) == boundary) || (numberOfSides[position + 1] < 2)) && ((board[position] & 2) == 0);
                boolean down = (((position / size) == boundary) || (numberOfSides[position + size] < 2)) && ((board[position] & 4) == 0);
                boolean top = (((position / size) == 0) || (numberOfSides[position - size] < 2)) && ((board[position] & 1) == 0);
                final Edge[] candidates = new Edge[4];
                int lines = 0;
                if (top) {
                    candidates[lines++] = getEdge(position, 0);
                }
                if (down) {
                    candidates[lines++] = getEdge(position, 2);
                }
                if (right) {
                    candidates[lines++] = getEdge(position, 1);
                }
                if (left) {
                    candidates[lines++] = getEdge(position, 3);
                }
                if (lines > 0) {
                    if (!randomize) {
                        return candidates[0];
                    } else {
                        return candidates[random.nextInt(lines)];
                    }
                }
            }
        }
        return null;
    }

    private Edge findEndOfChain(final Chain chain) {
        int position;
        if (chain.side == 0) {
            position = chain.start - size;
        } else if (chain.side == 1) {
            position = chain.start + 1;
        } else if (chain.side == 2) {
            position = chain.start + size;
        } else {
            position = chain.start - 1;
        }
        if ((board[position] & 8) == 0 && chain.side != 1) {
            return getEdge(position, 3);
        } else if ((board[position] & 1) == 0 && chain.side != 2) {
            return getEdge(position, 0);
        } else if ((board[position] & 4) == 0 && chain.side != 0) {
            return getEdge(position, 2);
        } else if ((board[position] & 2) == 0 && chain.side != 3) {
            return getEdge(position, 1);
        }
        throw new IllegalStateException("Should have an open side");
    }

    private Edge getEdge(int i, int k) {
        return new Edge(i / size, i % size, k);
    }


    private List<Chain> findAllChains() {
        final List<Chain> chains = new ArrayList<Chain>(5);
        for (int index = 0; index < boardSize; index++) {
            final int position = order.get(index);
            if ((visited & (1 << position)) == 0 && (counts[3] & (1 << position)) != 0) {
                boolean open = false;
                final int q[] = new int[boardSize];
                int front = 0, rear = 0, excess = 0;
                visited |= (1 << position);
                if ((board[position] & 8) == 0 && position % size != 0 && (visited & (1 << (position - 1))) == 0) {
                    q[rear++] = position - 1;
                    visited |= (1 << (position - 1));
                }
                if ((board[position] & 1) == 0 && position / size != 0 && (visited & (1 << (position - size))) == 0) {
                    q[rear++] = position - size;
                    visited |= (1 << (position - size));
                }
                if ((board[position] & 4) == 0 && position / size != boundary && (visited & (1 << (position + size))) == 0) {
                    q[rear++] = position + size;
                    visited |= (1 << (position + size));
                }
                if ((board[position] & 2) == 0 && position % size != boundary && (visited & (1 << (position + 1))) == 0) {
                    q[rear++] = position + 1;
                    visited |= (1 << (position + 1));
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
                chains.add(new Chain(position, findSide(position), open, rear + 1 - excess));
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
}