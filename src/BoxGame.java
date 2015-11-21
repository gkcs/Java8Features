import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoxGame {
    private static final String A = "A", B = "B", NEUTRAL = "-";
    private static final String[] sides = new String[]{"TOP", "RIGHT", "DOWN", "LEFT"};
    private static final String[][] strings = new String[5][5];

    public static void main(String args[]) {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            strings[i / 5][i % 5] = NEUTRAL;
            order.add(i);
        }
        Collections.shuffle(order);
        int[][] field = new int[5][5];
        int first = 0, second = 0;
        boolean turn = true;
        for (int i = 0; i < 60; i++) {
            System.out.println("MOVE NUMBER: " + i);
            System.out.println("TURN: " + (turn ? A : B));
            Edge move;
            if (turn) {
                Board board = new Board(field, order);
                if (!board.findAnyAlmostCompleteSquare() && board.move == null) {
                    if (!board.findLeastCompletedSquare() && board.move == null) {
                        board.findRandomSquare();
                    }
                }
                move = board.move;
                //move = new Strategy(5, false).input(field);
            } else {
                move = new Strategy(5, true).input(field);
            }
            updatedBoard(field, move);
            field[move.x][move.y] |= (1 << move.side);
            int score = 0;
            String player = turn ? A : B;
            if (move.side == 0 && move.x != 0 && field[move.x - 1][move.y] == 15) {
                score++;
                strings[move.x - 1][move.y] = player;
            } else if (move.side == 1 && move.y != 4 && field[move.x][move.y + 1] == 15) {
                score++;
                strings[move.x][move.y + 1] = player;
            } else if (move.side == 2 && move.x != 4 && field[move.x + 1][move.y] == 15) {
                score++;
                strings[move.x + 1][move.y] = player;
            } else if (move.side == 3 && move.y != 0 && field[move.x][move.y - 1] == 15) {
                score++;
                strings[move.x][move.y - 1] = player;
            }
            if (field[move.x][move.y] == 15) {
                score++;
                strings[move.x][move.y] = player;
            }
            if (score > 0) {
                turn = !turn;
                if (turn) {
                    first = first + score;
                } else {
                    second = second + score;
                }
            }
            printBoard(strings);
            printMove(move);
            System.out.println("FIRST: " + first);
            System.out.println("SECOND: " + second);
            turn = !turn;
        }
    }

    private static void printMove(Edge move) {
        System.out.println("MAKING MOVE AT: " + (move.x + 1) + ", " + (move.y + 1) + " " + sides[move.side]);
    }

    private static void printBoard(String[][] field) {
        for (String[] row : field) {
            for (String owner : row) {
                System.out.print(owner + " ");
            }
            System.out.println();
        }

    }

    private static void updatedBoard(int[][] field, Edge move) {
        if (move.side == 0) {
            if (move.x > 0) {
                field[move.x - 1][move.y] |= 4;
            }
        } else if (move.side == 1) {
            if (move.y < 4) {
                field[move.x][move.y + 1] |= 8;
            }
        } else if (move.side == 2) {
            if (move.x < 4) {
                field[move.x + 1][move.y] |= 1;
            }
        } else {
            if (move.y > 0) {
                field[move.x][move.y - 1] |= 2;
            }
        }
    }
}
