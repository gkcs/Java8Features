import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class MainTest {

//            1
//            3 4
//            1 3 5
//            1 4 3
//            1 1 3
//            2 6 22
//            3 5 8

    @Test
    public void test() {
        final Solver solver = new Solver();
        System.out.println(solver.solve(
                new int[]{1, 3, 5},
                4,
                new int[]{1, 1, 2, 3},
                new int[]{4, 1, 6, 5},
                new int[]{3, 3, 22, 8}));
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