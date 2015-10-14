package ml;

import java.math.BigInteger;
import java.util.Arrays;

public class LCMFinder {
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

    private final int primes[] = new int[9592];
    private final int mod = 1000000007;

    private int primeCount = 0;

    private final long[] primeProduct, inverses;

    LCMFinder(int n) {
        final boolean sieve[] = new boolean[n + 1];
        primes[primeCount++] = 2;
        final int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i = i + 2) {
            if (!sieve[i]) {
                sieve[i] = true;
                primes[primeCount++] = i;
                for (int j = i + (i << 1); j <= n; j = j + (i << 1)) {
                    sieve[j] = true;
                }
            }
        }
        for (int i = ((sqrt & 1) == 0) ? (sqrt + 1) : sqrt; i <= n; i = i + 2) {
            if (!sieve[i]) {
                primes[primeCount++] = i;
            }
        }
        primeProduct = new long[primeCount + 1];
        inverses = new long[primeProduct.length];
        primeProduct[0] = 1;
        final BigInteger modo = BigInteger.valueOf(1000000007);
        inverses[0] = BigInteger.ONE.modInverse(modo).intValue();
        for (int i = 1; i <= primeCount; i++) {
            primeProduct[i] = (primeProduct[i - 1] * primes[i - 1]) % mod;
            inverses[i] = (inverses[i - 1] * BigInteger.valueOf(primes[i - 1]).modInverse(modo).intValue()) % mod;
        }
    }

    public int lcm(int n, int k) {
        k = n - k + 1;
        long rangers = Math.sqrt(n) < (n - k) ? (primeProduct[findSmallerOrEqualTo(n - k)]
                * inverses[findSmallerOrEqualTo((int) Math.sqrt(n))]) % mod : 1;
        final int start = (n - k + 1 > k) ? (n - k + 1) : k;
        if (start <= n) {
            rangers = rangers * (primeProduct[findSmallerOrEqualTo(n)] * inverses[findSmallerOrEqualTo(start - 1)]) % mod;
        }
        int last = findSmallerOrEqualTo((int) Math.sqrt(n)) - 1;
        for (int i = 0; i <= last; i++) {
            rangers = (rangers * pow(primes[i], k, n)) % mod;
        }
        int end = (n / 2 > k - 1) ? (k - 1) : n / 2;
        int begin = Math.sqrt(n) < (n - k) ? (n - k) : (int) Math.sqrt(n);
        if (end >= begin) {
            begin = findLargerOrEqualTo(begin + 1);
            end = findSmallerOrEqualTo(end);
            for (int i = begin; i < end; i++) {
                if (k / primes[i] != n / primes[i]) {
                    rangers = (rangers * primes[i]) % mod;
                }
            }
        }
        System.out.println("INSIDE");
        int j=0;j=3;
        j++;
        System.out.println(j);
        return (int) rangers;
    }

    public int findSmallerOrEqualTo(int key) {
        int index = Arrays.binarySearch(primes, 0, primeCount, key);
        if (index < 0) {
            index = -index - 1;
            index--;
        }
        return index + 1;
    }

    public int findLargerOrEqualTo(int key) {
        int index = Arrays.binarySearch(primes, 0, primeCount, key);
        if (index < 0) {
            index = -index - 1;
        }
        return index;
    }

    public long pow(final int number, final int start, final int end) {
        if (number > end) {
            return 1;
        }
        if (start == end) {
            long temp = number;
            while (start % temp == 0) {
                temp = temp * number;
            }
            return temp / number;
        } else {
            long temp = number;
            while (end - start >= temp) {
                temp = temp * number;
            }
            temp = temp / number;
            //we now have the smallest power we should start from
            while (end / temp != start / temp) {
                temp = temp * number;
            }
            temp = temp / number;
            //we now have a number raised to zero coefficient diff
            if (start % temp == 0) {
                while (start % temp == 0) {
                    temp = temp * number;
                }
                temp = temp / number;
            }
            return temp;
        }
    }
}