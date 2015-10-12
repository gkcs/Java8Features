import org.junit.Test;

import java.util.Random;

public class MainTest {

    public static final int MAX = 100000;

    @Test
    public void test() {
        //final int cities = 100000, routes = 100000, weights = 100000, costs = 100000, distances = 100000;
        final int cities = 100, routes = 100, weights = 100, costs = 100, distances = 100;
        final int from[] = new int[routes];
        final int to[] = new int[routes];
        final int distance[] = new int[routes];
        final Random random = new Random();
        for (int i = 0; i < routes; i++) {
            from[i] = random.nextInt(cities) + 1;
            to[i] = random.nextInt(cities) + 1;
            distance[i] = random.nextInt(distances) + 1;
        }
        final int numberOfOrders = 100;
        final int source[] = new int[routes];
        final int destination[] = new int[routes];
        final int weight[] = new int[routes];
        final int income[] = new int[routes];
        for (int i = 0; i < numberOfOrders; i++) {
            source[i] = random.nextInt(cities) + 1;
            destination[i] = random.nextInt(cities) + 1;
            weight[i] = random.nextInt(weights) + 1;
            income[i] = random.nextInt(costs) + 1;
        }
        final Solver solver = new Solver(cities, routes, from, to, distance, numberOfOrders, source, destination, weight, income);
        System.out.println(solver.solve(random.nextInt(cities) + 1, MAX, MAX));
    }
}

// import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.Random;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//
//public class MainTest {
//
//    public static final int N = 100000;
//
//    @Test
//    public void test() {
//        final Solver solver = new Solver(N);
//        final BruteForceSolver bruteForceSolver = new BruteForceSolver(N);
//        final int n = 2000, k = 1000;
//        assertEquals(bruteForceSolver.lcm(n, k), solver.lcm(n, k));
//    }
//
//    @Test
//    public void test1() {
//        final Solver solver = new Solver(N);
//        final BruteForceSolver bruteForceSolver = new BruteForceSolver(N);
//        final int n = 7, k = 1;
//        System.out.println(n + " " + k);
//        assertEquals(bruteForceSolver.lcm(n, k), solver.lcm(n, k));
//    }
//
//    @Test
//    public void test3() {
//        final Solver solver = new Solver(N);
//        final BruteForceSolver bruteForceSolver = new BruteForceSolver(N);
//        final int n = 97, k = 47;
//        System.out.println(n + " " + k);
//        assertEquals(bruteForceSolver.lcm(n, k), solver.lcm(n, k));
//    }
//
//    @Test
//    public void test2() {
//        final Solver solver = new Solver(N);
//        final BruteForceSolver bruteForceSolver = new BruteForceSolver(N);
//        final int n = N, k = N;
//        assertEquals(bruteForceSolver.lcm(n, k), solver.lcm(n, k));
//    }
//
//    @Test
//    public void testRandom() {
//        final Solver solver = new Solver(N);
//        final BruteForceSolver bruteForceSolver = new BruteForceSolver(N);
//        for (int i = 0; i < 100; i++) {
//            final Random random = new Random();
//            final int n = random.nextInt(100) + 1, k = random.nextInt(n) + 1;
//            System.out.println(n + " " + k);
//            assertEquals(bruteForceSolver.lcm(n, k), solver.lcm(n, k));
//        }
//    }
//
//    @Test
//    public void speed() {
//        final Solver solver = new Solver(N);
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < 1000000; i++) {
//            final Random random = new Random();
//            final int n = random.nextInt(100000) + 1, k = random.nextInt(n) + 1;
//            stringBuilder.append(solver.lcm(n, k)).append('\n');
//        }
//        System.out.println(stringBuilder);
//    }
//
//    @Test
//    public void check() {
//        final Solver solver = new Solver(N);
//        final BruteForceSolver bruteForceSolver = new BruteForceSolver(N);
//        final Random random = new Random();
//        final int t = 1000, N1 = 50, K1 = 7, M = 10000, A = 0, B = 0;
//        final int C[] = new int[t - 1];
//        final int D[] = new int[t - 1];
//        for (int i = 1; i < t; i++) {
//            C[i - 1] = random.nextInt(N);
//            D[i - 1] = random.nextInt(N);
//        }
//        assertArrayEquals(bruteForceSolver.solve(t, N1, K1, A, B, M, C, D), solver.solve(t, N1, K1, A, B, M, C, D));
//    }
//
//    @Test
//    public void speedCheck() {
//        final int M=10000;
//        final Solver solver = new Solver(M);
//        final Random random = new Random();
//        final int t = 1000, N1 = random.nextInt(M)+1, K1 = random.nextInt(N1)+1, A = 0, B = 0;
//        final int C[] = new int[t - 1];
//        final int D[] = new int[t - 1];
//        for (int i = 1; i < t; i++) {
//            C[i - 1] = random.nextInt(M);
//            D[i - 1] = random.nextInt(M);
//        }
//        System.out.println(Arrays.toString(solver.solve(t, N1, K1, A, B, M, C, D)));
//    }
//
//}

//import java.io.IOException;
//
//public class Main {
//
//    public static void main(String args[]) throws IOException {
//        final InputReader br = new InputReader(System.in);
//        final StringBuilder stringBuilder = new StringBuilder();
//        final Solver solver = new Solver();
//        final int n = br.readInt(), m = br.readInt();
//        final int[] x = new int[m], y = new int[m], l = new int[m];
//        for (int i = 0; i < m; i++) {
//            x[i] = br.readInt();
//            y[i] = br.readInt();
//            l[i] = br.readInt();
//        }
//        final int k = br.readInt();
//        final int[] a = new int[k], b = new int[k], w = new int[k], income = new int[k];
//        for (int i = 0; i < k; i++) {
//            a[i] = br.readInt();
//            b[i] = br.readInt();
//            w[i] = br.readInt();
//            income[i] = br.readInt();
//        }
//        final int S = br.readInt(), D = br.readInt(), W = br.readInt();
//        solver.solve(n, m, x, y, l, k, a, b, w, income, S, D, W);
//        System.out.println(stringBuilder.toString());
//    }
//
//}
//
//class Solver {
//    public int solve(final int n,
//                     final int m,
//                     final int[] x,
//                     final int[] y,
//                     final int[] l,
//                     final int k,
//                     final int[] a,
//                     final int[] b,
//                     final int[] w,
//                     final int[] income,
//                     final int S,
//                     final int D,
//                     final int W) {
//        return 0;
//    }
//}
//
//} else {
//final int answers[] = new int[t];
//final Query queries[] = new Query[t];
//        queries[0] = new Query(N1, K1, 0);
//        for (int i = 1; i < t; i++) {
//final int N = 1 + C[i - 1] % M;
//final int K = 1 + D[i - 1] % N;
//        queries[i] = new Query(N, K, i);
//        }
//        Arrays.parallelSort(queries);
//        BigInteger answer = BigInteger.ONE;
//        int s = 0, e = 0, scurrent;
//        for (int i = 0; i < t; i++) {
//        if (s == queries[i].start) {
//        scurrent = e + 1;
//        } else {
//        answer = BigInteger.ONE;
//        scurrent = queries[i].start;
//        }
//        for (int j = scurrent; j <= queries[i].end; j++) {
//        BigInteger val = BigInteger.valueOf(j);
//        answer = answer.multiply(val).divide(gcd(answer, val));
//        }
//        answers[queries[i].index] = answer.mod(modo).intValue();
//        s = queries[i].start;
//        e = queries[i].end;
//        }
//        return answers;
//        }
//        }
//
//private BigInteger gcd(BigInteger answer, BigInteger val) {
//        while (!val.equals(BigInteger.ZERO)) {
//        BigInteger temp = val;
//        val = answer.mod(val);
//        answer = temp;
//        }
//        return answer;
//        }