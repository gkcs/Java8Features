import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class BoxGame {
    public static void main(String args[]) {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        int[][] field = new int[5][5];
        boolean moves[][][] = new boolean[5][5][4];
        HashSet<Point> hashSet = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Board board = new Board(field, order);
            if (!board.findAnyAlmostCompleteSquare() && board.move == null) {
                if (!board.findLeastCompletedSquare() && board.move == null) {
                    board.findRandomSquare();
                }
            }
            if (moves[board.move.x][board.move.y][board.move.side]) {
                throw new RuntimeException();
            }
            moves[board.move.x][board.move.y][board.move.side] = true;
            if (hashSet.contains(board.move)) {
                throw new IllegalStateException("NO NO NO!!");
            } else {
                hashSet.add(board.move);
            }
            System.out.println("MOVE NUMBER: " + i);
            System.out.println(board.move);
            field[board.move.x][board.move.y] |= (1 << board.move.side);
            if (board.move.side == 0) {
                if (board.move.x > 0) {
                    field[board.move.x - 1][board.move.y] |= 4;
                }
            } else if (board.move.side == 1) {
                if (board.move.y < 4) {
                    field[board.move.x][board.move.y + 1] |= 8;
                }
            } else if (board.move.side == 2) {
                if (board.move.x < 4) {
                    field[board.move.x + 1][board.move.y] |= 1;
                }
            } else {
                if (board.move.y > 0) {
                    field[board.move.x][board.move.y - 1] |= 2;
                }
            }
            System.out.println(board);
        }
    }
}
