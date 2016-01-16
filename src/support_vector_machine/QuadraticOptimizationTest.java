package support_vector_machine;

import org.junit.Test;

import java.util.Arrays;

public class QuadraticOptimizationTest {

    private QuadraticOptimization quadraticOptimization = new QuadraticOptimization();

    @Test
    public void test() {
        System.out.println(Arrays.deepToString(
                quadraticOptimization.solve(
                        new double[][]{
                                {0, 2},
                                {1, 1},
                                {2, 0},
                                {0, 3},
                                {3, 3}},
                        new double[][]{{1}, {1}, {1}, {-1}, {-1}})));
    }

    @Test
    public void test1() {
        System.out.println(Arrays.deepToString(
                quadraticOptimization.solve(
                        new double[][]{
                                {1, 0},
                                {2, 0},
                                {1, 2},
                                {2, 2}},
                        new double[][]{{1}, {1}, {-1}, {-1}})));
    }

    @Test
    public void test2() {
        System.out.println(Arrays.deepToString(
                quadraticOptimization.solve(
                        new double[][]{
                                {1, 0},
                                {1, 2}},
                        new double[][]{{1}, {-1}})));
    }

    @Test
    public void test3() {
        System.out.println(Arrays.toString(quadraticOptimization.classify(new double[][]{{2, 2}},
                new double[][]{{1, 0}, {1, 2}},
                new double[]{1, -1}, new double[]{1, 1})));
    }
}
