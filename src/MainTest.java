import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class MainTest {

    private final Solver solver = new Solver();
    private final BruteForceSolver bruteForceSolver = new BruteForceSolver();

    @Test
    public void test() {
        final int board[][] = new int[][]{{13, 3, 8}, {3, 14, 11}, {0, 3, 10}};
        final Strategy strategy = new Strategy(board.length);
        System.out.println(strategy.input(board));
    }

    @Ignore
    @Test
    public void testRandom() {
        final Random random = new Random();
    }
}