import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MainTest {
    private Solver solver;

    @Before
    public void setUp() {
        solver = new Solver();
    }

    @Test
    public void test() {
        System.out.println(solver.solve(new int[]{0, 0}, 0));
    }

    @Ignore
    @Test
    public void testPow() {
        for (int i = 0; i < 35; i++) {
            System.out.println(solver.powMod(i));
        }
    }
}