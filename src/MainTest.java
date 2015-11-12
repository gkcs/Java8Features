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
                new int[]{1, 1, 2, 3},
                new int[]{4, 1, 6, 5},
                new int[]{3, 3, 22, 8}));
    }


    @Test
    public void test1() {
        final Solver solver = new Solver();
        System.out.println(solver.solve(
                new int[]{1, 2, 1, 5, 1, 1},
                new int[]{2, 1},
                new int[]{1, 5},
                new int[]{2, 2}));
    }

    @Ignore
    @Test
    public void testRandom() {
        final Solver solver = new Solver();
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            final int cards[] = new int[random.nextInt(100000) + 1];
            for (int j = 0; j < cards.length; j++) {
                cards[j] = random.nextInt(50000) + 1;
            }
            final int m = random.nextInt(50000) + 1, t[] = new int[m], a[] = new int[m], b[] = new int[m];
            for (int j = 0; j < m; j++) {
                t[j] = random.nextInt(50000) + 1;
                a[j] = random.nextInt(50000) + 1;
                b[j] = random.nextInt(50000) + 1;
            }
            System.out.println(cards.length + " " + m);
            solver.solve(cards, t, a, b);
        }
    }
}