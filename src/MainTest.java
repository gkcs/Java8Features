import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MainTest {
    BruteForceSolver bruteForceSolver;
    Solver solver;

    @Before
    public void setUp() {
        bruteForceSolver = new BruteForceSolver();
        solver = new Solver();
    }

    @Test
    public void testRandomExists() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final StringBuilder text = new StringBuilder();
            for (int j = 0; j < 10000; j++) {
                text.append('a' + random.nextInt(26));
            }
            final String TEXT = text.toString();
            final String PATTERN = TEXT.substring(random.nextInt(500), random.nextInt(500) + 500);
            assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
        }
    }

    @Test
    public void testRandom() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final StringBuilder text = new StringBuilder();
            for (int j = 0; j < 10000; j++) {
                text.append('a' + random.nextInt(26));
            }
            final StringBuilder pattern = new StringBuilder();
            int n = random.nextInt(text.length());
            for (int j = 0; j < n; j++) {
                pattern.append('a' + random.nextInt(26));
            }
            final String TEXT = text.toString();
            final String PATTERN = pattern.toString();
            assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
        }
    }

    @Test
    public void test() {
        final String TEXT = "abcdefghijklmnopqrstuvwxyz";
        final String PATTERN = "defghij";
        assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
    }
}
