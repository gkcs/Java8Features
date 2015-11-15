import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoxGame {
    public static void main(String args[]) {
        List<Integer> order = new ArrayList<Integer>();
        for (int i = 0; i < 25; i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        int[][] field = new int[5][5];
        Board board = new Board(field, order);
        if (!board.findAnyAlmostCompleteSquare() && board.move == null) {
            if (!board.findLeastCompletedSquare() && board.move == null) {
                board.findRandomSquare();
            }
        }
        System.out.println(board.move);
    }
}
