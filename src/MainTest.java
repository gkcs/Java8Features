import org.junit.Test;

import java.util.Random;

public class MainTest {

    @Test
    public void test() {
        final int board[][] = new int[][]{{5, 5, 5, 5, 5}, {3, 9, 5, 5, 3}, {6, 12, 5, 3, 10}, {3, 9, 5, 6, 12}, {12, 4, 7, 9, 3}};
        final Strategy strategy = new Strategy(board.length);
        System.out.println(strategy.input(board));
    }

    @Test
    public void testBoard() {
        final int board[][] = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        final Strategy strategy = new Strategy(board.length);
        System.out.println(strategy.input(board));
    }

    @Test
    public void test3() {
        final int board[][] = new int[][]{{5, 5, 5}, {5, 5, 5}, {5, 5, 5}};
        final Strategy strategy = new Strategy(board.length);
        System.out.println(strategy.input(board));
    }

    @Test
    public void testRandom() {
        final Random random = new Random();
    }
}