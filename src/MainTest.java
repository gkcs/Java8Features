import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class MainTest {
    Solver solver;

    @Before
    public void setUp() {
        solver = new Solver();
    }

    @Test
    public void test1() {
        assertArrayEquals(new int[]{-595, 100, 150, 221, 974}, solver.solve(new int[]{150, -595, 100, 974, 221}));
    }

    @Test
    public void test() {
        assertArrayEquals(new int[]{22, 44, 50, 97}, solver.solve(new int[]{97, 22, 50, 44}));
    }
}
