package ml;

public class LCMFinder {
    public static void main(String args[]) {
        final StringBuilder stringBuilder = new StringBuilder();
        final CorrelationFinder correlationFinder = new CorrelationFinder();
        final InputReader br = new InputReader(System.in);
        final int n = br.readInt();
        final int[] a = new int[n], b = new int[n];
        System.out.println("ENTER ATTRIBUTES:");
        for (int i = 0; i < n; i++) {
            a[i] = br.readInt();
        }
        System.out.println("ENTER RESULTS:");
        for (int i = 0; i < n; i++) {
            b[i] = br.readInt();
        }
        System.out.println(correlationFinder.findCorrelation(a, b));
    }
}