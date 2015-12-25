import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private Solver solver;
    private BruteForceSolver bruteForceSolver;

    @Before
    public void setUp() {
        solver = new Solver();
        bruteForceSolver = new BruteForceSolver();
    }

}