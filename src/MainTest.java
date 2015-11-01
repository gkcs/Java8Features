import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class MainTest {

    @Test
    public void test() {
        final Random random = new Random();
        final Solver solver = new Solver();
        for (int i = 0; i < 1000; i++) {
            solver.solve(BigInteger.valueOf(random.nextInt(Integer.MAX_VALUE) + 1L),
                    BigInteger.valueOf(random.nextInt(Integer.MAX_VALUE) + 1L),
                    BigInteger.valueOf(random.nextInt(Integer.MAX_VALUE) + 1L));
        }
    }
}