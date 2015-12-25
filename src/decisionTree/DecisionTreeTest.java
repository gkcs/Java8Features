package decisionTree;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecisionTreeTest {
    DecisionTree decisionTree;

    @Test
    public void test() throws IOException {
        final List<String[]> elements = new ArrayList<>();
        elements.add(new String[]{"2", "1", "D3", "10", "0.076923077", "2", "1", "1", "0.641791045", "0.581818182", "0.148535565", "0.323007976", "0.028", "12", "1", "0", "3", "", "1", "2", "6", "3", "1", "2", "1", "1", "1", "3", "1", "0.000666667", "1", "1", "2", "2", "", "0.598039216", "", "0.526785714", "4", "112", "2", "1", "1", "3", "2", "2", "1", "", "3", "2", "3", "3", "240", "3", "3", "1", "1", "2", "1", "2", "3", "", "1", "3", "3", "1", "3", "2", "3", "", "1", "3", "1", "2", "2", "1", "3", "3", "3", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "8"});
        elements.add(new String[]{"5", "1", "A1", "26", "0.076923077", "2", "3", "1", "0.059701493", "0.6", "0.131799163", "0.272287744", "0", "1", "3", "0", "2", "0.0018", "1", "2", "6", "3", "1", "2", "1", "2", "1", "3", "1", "0.000133333", "1", "3", "2", "2", "0.188405797", "", "0.084507042", "", "5", "412", "2", "1", "1", "3", "2", "2", "1", "", "3", "2", "3", "3", "0", "1", "3", "1", "1", "2", "1", "2", "3", "", "1", "3", "3", "1", "3", "2", "3", "", "3", "1", "1", "2", "2", "1", "3", "3", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "4"});
        elements.add(new String[]{"6", "1", "E1", "26", "0.076923077", "2", "3", "1", "0.029850746", "0.745454545", "0.288702929", "0.428780429", "0.03", "9", "1", "0", "2", "0.03", "1", "2", "8", "3", "1", "1", "1", "2", "1", "1", "3", "", "3", "2", "3", "3", "0.304347826", "", "0.225352113", "", "10", "3", "2", "2", "1", "3", "2", "2", "2", "", "3", "2", "3", "3", "", "1", "3", "1", "1", "2", "1", "2", "3", "", "2", "2", "3", "1", "3", "2", "3", "", "3", "3", "1", "3", "2", "1", "3", "3", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "8"});
        final int[] a = new int[DecisionTreeAlgorithm.NO_OF_DIFFERENT_ATTRIBUTES + 1];
        for (int i = 0; i < a.length; i++) {
            a[i] = elements.size();
        }
        decisionTree = new DecisionTree(elements, a);
        System.out.println(decisionTree);
    }

    @Test
    public void testFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/gaurav.se/Downloads/train.csv")));
        DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
        decisionTreeAlgorithm.makeDecisionTree(bufferedReader);
    }

    @Test
    public void findValues() throws IOException {
        DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
        decisionTreeAlgorithm.findDistinctValuesInData();
    }

    @Test
    public void verify() {
        int a[][] = new int[][]{{0, 6024, 6472, 1008, 1428, 5410, 11118, 8027, 19488}, {0, 183, 80, 5, 0, 22, 115, 0, 1}};
        double v[] = new double[]{58975, 406};
        for (int i = 0; i < a.length; i++) {
            double total = 0;
            for (int j = 0; j < a[i].length; j++) {
                double x = -a[i][j] / v[i] * Math.log(a[i][j] / v[i]) / Math.log(2);
                if (a[i][j] > 0) {
                    total += x;
                }
                System.out.println(x);
            }
            System.out.println("TOTAL: " + total);
        }
    }
}
