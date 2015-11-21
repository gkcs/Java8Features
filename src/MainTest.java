import org.junit.Test;

import java.util.Random;

public class MainTest {

    @Test
    public void test() {
        final int board[][] = new int[][]{{5, 3, 12}, {3, 12, 3}, {12, 3, 10}};
        final Strategy strategy = new Strategy(board.length);
        System.out.println(strategy.input(board));
    }

    @Test
    public void testRandom() {
        final Random random = new Random();
    }
}