import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class MainTest {

    private final Solver solver = new Solver();
    private final BruteForceSolver bruteForceSolver = new BruteForceSolver();

    @Test
    public void test9() {
        final int[] cards = {6, 8, 2, 6, 4};
        final int[] a = {4, 9, 8, 3, 9, 4, 4, 9, 10, 8};
        final int[] b = {6, 4, 6, 9, 5, 7, 3, 7, 1, 10};
    }

    @Ignore
    @Test
    public void testRandom() {
        final Random random = new Random();
    }
}