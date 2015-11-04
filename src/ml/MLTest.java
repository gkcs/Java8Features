package ml;

import org.junit.Test;

public class MLTest {
    @Test
    public void test() {
        final CorrelationFinder correlationFinder = new CorrelationFinder();
        final int[] a = {106, 86, 100, 101, 99, 103, 97, 113, 112, 110},
                b = {7, 0, 27, 50, 28, 29, 20, 12, 6, 17};
        System.out.println(correlationFinder.findCorrelation(a, b));
    }
}
