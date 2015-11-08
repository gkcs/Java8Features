import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void test() {
        final Solver solver = new Solver();
        assertEquals("6", solver.solve(1, 1, 1, 3));
    }

    @Test
    public void test1() {
        final Solver solver = new Solver();
        assertEquals("12", solver.solve(14, 7, 2, 4));
    }

    @Test
    public void test2() {
        final Solver solver = new Solver();
        assertEquals("7", solver.solve(14, 7, 9, 9));
    }

    @Ignore
    @Test
    public void testSum() {
        final Solver solver = new Solver();
        final Random random = new Random();
        int max = 1000000000;
        for (int i = 0; i < 1000; i++) {
            int right = random.nextInt(max) + 1;
            System.out.println(Long.parseLong(solver.solve(random.nextInt(max) + 1, random.nextInt(max), random.nextInt(right) + 1, right)));
        }
    }
}