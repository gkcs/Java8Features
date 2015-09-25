import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MainTest {
    BruteForceSolver bruteForceSolver = new BruteForceSolver();
    Solver solver = new Solver();

    @Test
    public void test() {
        int a[] = new int[]{3, 4, 5, 1, 1, 2};
        assertEquals(bruteForceSolver.solve(a), solver.solve(a[0]));
    }

    @Test
    public void test1() {
        int a[] = new int[]{3, 2, 9, 5, 2, 9, 4, 14, 7, 10};
        assertEquals(bruteForceSolver.solve(a), solver.solve(a[0]));
    }

    @Test
    public void test2() {
        int a[] = new int[]{14, 5, 13, 19, 17, 10, 18, 12};
        assertEquals(bruteForceSolver.solve(a), solver.solve(a[0]));
    }

    @Test
    public void testRandom() {
        final Random random = new Random();
        for (int t = 0; t < 100; t++) {
            final int a[] = new int[random.nextInt(10000)];
            for (int i = 0; i < a.length; i++) {
                a[i] = random.nextInt(5) + 1;
            }
            assertEquals(bruteForceSolver.solve(a), solver.solve(a[0]));
        }
    }
}
