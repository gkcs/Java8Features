import java.util.Arrays;

public class BruteForceSolver {
    public static final int MAX = 101;
    private final int queue[] = new int[MAX];
    private final int time[] = new int[queue.length];

    public long solve(final int[] cards, final int[] t, final int[] a, final int[] b) {
        final int counts[] = new int[MAX];
        for (int card : cards) {
            counts[card]++;
        }
        final Offer[][] exchanges = getOffers(counts.length, t.length, t, a, b);
        long total = 0;
        for (int i = counts.length - 1; i > 0; i--) {
            total += i * (long) counts[i];
            counts[i] = 0;
            for (int reachableCity : getReachableCities(i, new boolean[counts.length], exchanges)) {
                total += i * (long) counts[reachableCity];
                counts[reachableCity] = 0;
            }
        }
        return total;
    }

    private int[] getReachableCities(final int destination, final boolean[] visited, final Offer[][] offers) {
        int front = 0, rear = 1;
        queue[0] = destination;
        time[0] = MAX;
        while (front < rear) {
            int current = queue[front];
            visited[current] = true;
            //&& current > offers[current][i].second
            for (int i = 0; i < offers[current].length; i++) {
                if (!visited[offers[current][i].second] && time[front] >= offers[current][i].time) {
                    queue[rear] = offers[current][i].second;
                    time[rear] = offers[current][i].time;
                    rear++;
                }
            }
            front++;
        }
        final int reachableCities[] = new int[rear];
        System.arraycopy(queue, 0, reachableCities, 0, reachableCities.length);
//        if (destination <= 8) {
//            System.out.println("DESTINATION: " + destination);
//            System.out.println(Arrays.toString(reachableCities));
//        }
        return reachableCities;
    }

    private Offer[][] getOffers(int n, int m, int[] t, int[] a, int[] b) {
        final Offer[][] exchanges = new Offer[n][];
        final int offerCount[] = new int[n];
        final Offer[] offers = new Offer[m << 1];
        for (int i = 0; i < m; i++) {
            offers[i << 1] = new Offer(t[i], a[i], b[i]);
            offers[(i << 1) + 1] = new Offer(t[i], b[i], a[i]);
            offerCount[a[i]]++;
            offerCount[b[i]]++;
        }
        Arrays.parallelSort(offers);
        int start = 0;
        for (int i = 0; i < n; i++) {
            exchanges[i] = new Offer[offerCount[i]];
            System.arraycopy(offers, start, exchanges[i], 0, exchanges[i].length);
            start = start + exchanges[i].length;
        }
        return exchanges;
    }
}