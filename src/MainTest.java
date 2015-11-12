import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void test() {
        final Solver solver = new Solver();
        assertEquals(1, solver.solve(2, 0));
    }

    @Test
    public void test1() {
        final Solver solver = new Solver();
        assertEquals(2, solver.solve(2, 1));
    }

    @Test
    public void test2() {
        final Solver solver = new Solver();
        assertEquals(2, solver.solve(4, 2));
    }

    @Ignore
    @Test
    public void testSum() {
        final Solver solver = new Solver();
        final Random random = new Random();
        int max = 1000000000;
        for (int i = 0; i < 1000; i++) {
            int right = random.nextInt(max) + 1;
        }
    }
}