public class BruteForceSolver {

    private static final int mod = 1000000007;

    public long solve(final int[] a, final int photo) {
        long total = 0;
        for (int i = 0; i < (1 << a.length); i++) {
            int result = 0;
            for (int j = 0; j < a.length; j++) {
                if ((i & (1 << j)) != 0) {
                    result ^= a[j];
                }
            }
            if (result == photo) {
                System.out.println(String.format("%20s", Long.toBinaryString(i)).replace(' ', '0'));
                total++;
            }
        }
        return total % mod;
    }
}