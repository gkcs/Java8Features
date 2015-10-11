package ml;

public class PrimeNumberGenerator {
    private int primes[] = new int[9592];

    public PrimeNumberGenerator() {
        primes[0] = 2;
        int count = 1;
        boolean seive[] = new boolean[100001];
        int SQRT = (int) Math.sqrt(seive.length);
        for (int i = 3; i <= SQRT; i = i + 2) {
            if (!seive[i]) {
                primes[count++] = i;
                for (int j = i + (i << 1); j < seive.length; j = j + (i << 1)) {
                    seive[j] = true;
                }
            }
        }
        System.out.println(count);
        for (int i = SQRT + 1; i < seive.length; i = i + 2) {
            if (!seive[i]) {
                primes[count++] = i;
            }
        }
    }
}
