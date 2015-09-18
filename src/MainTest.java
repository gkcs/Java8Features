import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MainTest {
    BruteForceSolver bruteForceSolver;
    Solver solver;

    @Before
    public void setUp() {
        bruteForceSolver = new BruteForceSolver();
        solver = new Solver();
    }

    @Test
    public void testRandomExists() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final StringBuilder text = new StringBuilder();
            for (int j = 0; j < 10000; j++) {
                text.append('a' + random.nextInt(26));
            }
            final String TEXT = text.toString();
            final String PATTERN = TEXT.substring(random.nextInt(500), random.nextInt(500) + 500);
            assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
        }
    }

    @Test
    public void testRandom() {
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            final StringBuilder text = new StringBuilder();
            for (int j = 0; j < 100; j++) {
                text.append('a' + random.nextInt(26));
            }
            final StringBuilder pattern = new StringBuilder();
            int n = random.nextInt(text.length()) + 1;
            for (int j = 0; j < n; j++) {
                pattern.append('a' + random.nextInt(26));
            }
            final String TEXT = text.toString();
            final String PATTERN = pattern.toString();
            assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
        }
    }

    @Test
    public void test1() {
        String TEXT="1161031091131071131081191081101151201191131121131171211151201031191051221091161061001011181021101141191041169910810198110101101971181071211151079811010411699107102117112106119105107981229811610911510111710211697122971031151191021161131061081211091101131211111041061099910911611610010397103";
        String PATTERN="10210810010410811112111610610911211312097119116103118102120971121221201191201081101111031111159799114971211081151131221011171131021019910211512011811211211698113120104110120103971101091081171121201221031111151129710311312111410210412110712112010811611210210011910611412010310010510011297101118102118103109121112119118103106991131201151111101161171151121191211061011041221171219911111411210412098105115971121221171121041089711011711811911210510411810912112210110311211912199111101105991201121081039712011611511698118102981211209910110310910098106115103115106981191169711697104118118116116106102114122103106102";
        assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
    }

    @Test
    public void test() {
        final String TEXT = "abcdefghijklmnopqrstuvwxyz";
        final String PATTERN = "defghij";
        assertEquals(bruteForceSolver.solve(TEXT, PATTERN), solver.solve(TEXT, PATTERN));
    }
}
