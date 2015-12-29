package support_vector_machine;

import org.junit.Test;

import java.util.Arrays;

public class SimplexTest {
    private final Simplex simplex = new Simplex();

    @Test
    public void test() {
        System.out.println(Arrays.deepToString(simplex.solve(new double[][]{{2, 0, -1, -1, 0, 0}, {0, 3, -1, 0, -1, 0, 0}, {-1, -1, 0, 0, 0, 1}},
                new double[]{1, 1, -6},
                new int[][]{{0, 3, 0}, {1, 4, 0}},
                new double[]{0, 0, 0, 0, 0, 0},
                true)));
    }

    @Test
    public void test1() {
        System.out.println(Arrays.deepToString(simplex.solve(new double[][]{{1, 1, 1, 0}, {3, 2, 0, 1}}, new double[]{5, 12}, new int[0][0], new double[]{6, 5, 0, 0}, false)));
    }

    @Test
    public void test2() {
        System.out.println(Arrays.deepToString(simplex.solve(new double[][]{{1, 1, 1, 0, 0}, {2, 3, 0, 1, 0}, {1, 5, 0, 0, 1}},
                new double[]{10, 25, 35},
                new int[0][0],
                new double[]{6, 8, 0, 0, 0},
                false)));
    }

    @Test
    public void test3() {
        System.out.println(Arrays.deepToString(simplex.solve(new double[][]{{2, 3, -1, 0, 1, 0}, {5, 2, 0, -1, 0, 1}},
                new double[]{8, 12},
                new int[0][0],
                new double[]{-3, -4, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE},
                false)));
    }

}
