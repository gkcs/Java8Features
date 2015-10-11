import java.util.Arrays;

public class BruteForceSolver {
    private final Segment lcms;
    private final int mod = 1000000007;

    BruteForceSolver(int n) {
        lcms = new Segment(n);
    }

    public int lcm(final int n, final int k) {
        int[][] result = lcms.handleQuery(n - k + 1, n);
        long ans = 1;
        for (int i = 0; i < result[0].length; i++) {
            ans = (ans * pow(result[0][i], result[1][i])) % mod;
        }
        return (int) ans;
    }

    private long pow(final int number, final int exponent) {
        if (exponent == 1) {
            return number;
        }
        final long exp[] = new long[17];
        exp[0] = number;
        long result = ((exponent & 1) == 0) ? 1 : number;
        for (int i = 1; i < 32 && exponent >= (1 << i); i++) {
            exp[i] = (exp[i - 1] * exp[i - 1]) % mod;
            if ((exponent & (1 << i)) != 0) {
                result = (result * exp[i]) % mod;
            }
        }
        return result;
    }

    public int[] solve(final int t, final int N1, final int K1, final int A, final int B, final int M, final int[] C, final int D[]) {
        final int answers[] = new int[t];
        answers[0] = lcm(N1, K1);
        for (int i = 1; i < t; i++) {
            final int N = 1 + (int) ((A * (long) answers[i - 1] + C[i - 1]) % M);
            final int K = 1 + (int) ((B * (long) answers[i - 1] + D[i - 1]) % N);
            answers[i] = lcm(N, K);
        }
        return answers;
    }
}

class Segment {
    private final int a[][][];
    private int digit;

    public Segment(final int n) {
        digit = 0;
        while (n > (1 << digit)) {
            digit++;
        }
        a = new int[1 << (digit + 1)][2][];
        final int end = (1 << digit) + n, start = 1 << digit;
        final int counts[] = new int[n + 1];
        final boolean sieve[] = new boolean[n + 1];
        final int sqrt = (int) Math.sqrt(n);
        final int factors[][][] = new int[n + 1][2][6];
        for (int i = 2; i <= sqrt; i++) {
            if (!sieve[i]) {
                sieve[i] = true;
                for (int j = i; j <= n; j = j + i) {
                    sieve[j] = true;
                    factors[j][0][counts[j]] = i;
                    int temp = j;
                    while (temp % i == 0) {
                        temp = temp / i;
                        factors[j][1][counts[j]]++;
                    }
                    counts[j]++;
                }
            }
        }
        for (int i = sqrt; i <= n; i++) {
            if (counts[i] == 0) {
                for (int j = i; j <= n; j = j + i) {
                    factors[j][0][counts[j]] = i;
                    factors[j][1][counts[j]]++;
                    counts[j]++;
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            factors[i][0] = Arrays.copyOf(factors[i][0], counts[i]);
            factors[i][1] = Arrays.copyOf(factors[i][1], counts[i]);
        }
        System.arraycopy(factors, 1, a, start, end - start);
        for (int i = end; i < start * 2; i++) {
            a[i] = new int[0][];
        }
        buildTree(1);
    }

    private int[][] findLcm(int node, int left, int right, int gleft, int gright) {
        if (left <= right) {
            if (gleft >= left && gright <= right) {
                return a[node];
            } else if ((left >= gleft && gright >= left) || (right >= gleft && gright >= right)) {
                return lcm(findLcm(node << 1, left, right, gleft, (gright + gleft) / 2),
                        findLcm((node << 1) + 1, left, right, ((gright + gleft) / 2) + 1, gright));
            }
        }
        return new int[0][];
    }

    private int[][] buildTree(int node) {
        if ((node << 1) < a.length) {
            int[][] lcm = lcm(buildTree(node << 1), buildTree((node << 1) + 1));
            a[node] = lcm;
            return lcm;
        } else {
            return a[node];
        }
    }

    private int[][] lcm(int[][] first, int[][] second) {
        if (first.length == 0) {
            return second;
        }
        if (second.length == 0) {
            return first;
        }
        final int result[][] = new int[2][first[0].length + second[0].length];
        int count = 0;
        int i = 0, j = 0;
        for (; i < first[0].length && j < second[0].length; ) {
            if (first[0][i] == second[0][j]) {
                result[0][count] = first[0][i];
                if (first[1][i] > second[1][j]) {
                    result[1][count] = first[1][i];
                } else {
                    result[1][count] = second[1][j];
                }
                i++;
                j++;
                count++;
            } else if (first[0][i] < second[0][j]) {
                result[0][count] = first[0][i];
                result[1][count++] = first[1][i++];
            } else {
                result[0][count] = second[0][j];
                result[1][count++] = second[1][j++];
            }
        }
        if (i < first[0].length) {
            System.arraycopy(first[0], i, result[0], count, first[0].length - i);
            System.arraycopy(first[1], i, result[1], count, first[1].length - i);
            count = count + first[1].length - i;
        } else if (j < second[0].length) {
            System.arraycopy(second[0], j, result[0], count, second[0].length - j);
            System.arraycopy(second[1], j, result[1], count, second[1].length - j);
            count = count + second[0].length - j;
        }
        result[0] = Arrays.copyOf(result[0], count);
        result[1] = Arrays.copyOf(result[1], count);
        return result;
    }

    public int[][] handleQuery(int l, int r) {
        return findLcm(1, (1 << digit) + l - 1, (1 << digit) + r - 1, 1 << digit, (1 << (digit + 1)) - 1);
    }
}  