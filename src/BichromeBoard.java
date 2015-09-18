import java.util.Arrays;

public class BichromeBoard {
    private final int a[];
    private final long frequencies[];

    public BichromeBoard(int[] a) {
        frequencies = new long[a.length];
        int b[] = new int[a.length];
        System.arraycopy(a, 0, b, 0, a.length);
        Arrays.sort(b);
        this.a = b;
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; j++) {
                int max = -1;
                for (int k = i; k <= j; k++) {
                    if (a[k] > max) {
                        max = a[k];
                    }
                }
                frequencies[Arrays.binarySearch(b, max)]++;
            }
        }
    }

    public long solve(int filter, int val) {
        long sum = 0;
        if (filter == '>') {
            for (int i = 0; i < a.length; i++) {
                if (a[i] > val) {
                    sum += frequencies[i];
                }
            }
        } else if (filter == '<') {
            for (int i = 0; i < a.length; i++) {
                if (a[i] < val) {
                    sum += frequencies[i];
                }
            }
        } else {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == val) {
                    sum += frequencies[i];
                }
            }
        }
        return sum;
    }

}