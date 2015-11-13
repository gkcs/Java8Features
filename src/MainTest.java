import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

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
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        int[] cards = {1, 3, 5};
        int[] t = {1, 1, 2, 3};
        int[] a = {4, 1, 6, 5};
        int[] b = {3, 3, 22, 8};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }


    @Test
    public void test1() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        int[] cards = {1, 2, 1, 5, 1, 1};
        int[] t = {8, 1, 7, 2};
        int[] a = {8, 6, 1, 1};
        int[] b = {3, 2, 3, 5};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test2() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        int[] cards = {8, 6, 8, 8, 6, 7};
        int[] t = {11, 5, 9, 1, 15, 4, 12, 4, 10, 18};
        int[] a = {1, 1, 3, 5, 8, 7, 8, 9, 6, 1};
        int[] b = {6, 10, 8, 8, 10, 7, 6, 1, 7, 6};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test3() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        int[] cards = {6, 5, 4, 8, 5, 3, 4, 9, 7};
        int[] t = {8, 4, 6, 9};
        int[] a = {8, 6, 9, 6};
        int[] b = {2, 2, 2, 6};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test4() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        int[] cards = {2};
        int[] t = {4, 3};
        int[] a = {1, 1};
        int[] b = {3, 2};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test5() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        final int[] cards = {1, 1, 4, 2};
        final int[] t = {1, 4, 4, 1};
        final int[] a = {3, 1, 2, 2};
        final int[] b = {4, 2, 3, 4};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test6() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        final int[] cards = {10, 8, 2, 7, 4, 9, 6};
        final int[] t = {3, 5, 1, 4, 10, 6, 5, 9, 9, 5};
        final int[] a = {10, 4, 8, 9, 9, 2, 5, 2, 2, 7};
        final int[] b = {2, 2, 6, 6, 5, 8, 8, 9, 6, 5};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test7() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        final int[] cards = {6, 4, 5, 7, 3, 7, 3, 9};
        final int[] t = {1, 8, 8, 5, 5, 5, 1, 5, 4};
        final int[] a = {6, 1, 4, 8, 3, 6, 1, 6, 8};
        final int[] b = {7, 1, 7, 2, 2, 7, 1, 4, 2};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test8() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        final int[] cards = {6, 4, 5, 7, 3, 7, 3, 9};
        final int[] t = {1, 8, 5, 5, 5, 5, 4};
        final int[] a = {6, 4, 8, 3, 6, 6, 8};
        final int[] b = {7, 7, 2, 2, 7, 4, 2};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Test
    public void test9() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        final int[] cards = {6, 8, 2, 6, 4};
        final int[] t = {4, 2, 10, 10, 8, 1, 9, 1, 3, 8};
        final int[] a = {4, 9, 8, 3, 9, 4, 4, 9, 10, 8};
        final int[] b = {6, 4, 6, 9, 5, 7, 3, 7, 1, 10};
        assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
    }

    @Ignore
    @Test
    public void testRandom() {
        final Solver solver = new Solver();
        final BruteForceSolver bruteForceSolver = new BruteForceSolver();
        final Random random = new Random();
        int bound = Solver.MAX-1;
        for (int i = 0; i < 10000; i++) {
            final int cards[] = new int[random.nextInt(bound) + 1];
            int n = cards.length;
            for (int j = 0; j < n; j++) {
                cards[j] = random.nextInt(bound) + 1;
            }
            final int m = random.nextInt(bound) + 1, t[] = new int[m], a[] = new int[m], b[] = new int[m];
            for (int j = 0; j < m; j++) {
                t[j] = random.nextInt(bound) + 1;
                a[j] = random.nextInt(bound) + 1;
                b[j] = random.nextInt(bound) + 1;
            }
            System.out.println("TEST CASE:" + i);
            System.out.println(n + " " + m);
            System.out.println(Arrays.toString(cards));
            System.out.println(Arrays.toString(t));
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
            assertEquals(bruteForceSolver.solve(cards, t, a, b), solver.solve(cards, t, a, b));
        }
    }
}