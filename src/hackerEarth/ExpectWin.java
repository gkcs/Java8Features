package hackerEarth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExpectWin {
    private static final String FOLDER = "/Users/gaurav.se/Documents/will_bill_solve_it/";
    private static final String PATH = FOLDER;// + "test/";

    public static void main(String args[]) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH + "test_results.csv"));
        final BufferedReader bufferedReader1 = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/kaggle.csv"));
        final StringBuilder stringBuilder = new StringBuilder();
        bufferedReader.readLine();
        bufferedReader1.readLine();
        stringBuilder.append("Id,solved_status\n");
        String s = bufferedReader.readLine();
        String s1 = bufferedReader1.readLine();
        while (s != null && s1 != null && !s.equals("") && !s1.equals("")) {
            stringBuilder.append(s.split(",")[0]).append(',').append((s.split(",")[1].equals("1") || s1.split(",")[1].equals("1")) ? 1 : 0).append('\n');
            s = bufferedReader.readLine();
            s1 = bufferedReader1.readLine();
        }
        System.out.println(stringBuilder.toString());
    }
}
