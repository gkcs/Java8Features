package hackerEarth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttemptTillCorrectPredictor {
    public static void main(String args[]) throws IOException {
lastIsCorrect();
    }

    private static void lastIsCorrect() throws IOException {
        final List<String[]> csv = new ArrayList<>();
        HashMap<Submission, Integer> occurences = new HashMap<>();
        {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/train/submissions.csv"));
            bufferedReader.readLine();

            String s = bufferedReader.readLine();
            while (s != null && !s.equals("")) {
                String[] split = s.split(",");
                split[0] = "U" + split[0];
                split[1] = "P" + split[1];
                csv.add(split);
                Submission submission = new Submission(split[0], split[1]);
                occurences.put(submission, occurences.getOrDefault(submission, 0) + 1);
                s = bufferedReader.readLine();
            }
            bufferedReader.close();
            occurences.values().stream().distinct().sorted().forEach(System.out::println);
        }
        {
            final BufferedReader tests = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/train/submissions.csv"));
            tests.readLine();
            String test = tests.readLine();
            HashMap<Submission, Integer> attempts = new HashMap<>();
            int error = 0;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Id,solved_status\n");
            int hadWonEarlier = 0, neverWon = 0;
            while (test != null && !test.equals("")) {
                String[] split = test.split(",");
                split[0] = "U" + split[0];
                split[1] = "P" + split[1];
                Submission submission = new Submission(split[0], split[1]);
                attempts.put(submission, attempts.getOrDefault(submission, 0) + 1);
                if (attempts.get(submission)>=((occurences.get(submission)))) {
                    int mod = mod(1 - getResult(split[2], split[3]));
                    error += mod;
                    if (mod > 0) {
                        hadWonEarlier++;
                    }
                    stringBuilder.append(split[0]).append(',').append(1).append('\n');
                } else {
                    int mod = mod(0 - getResult(split[2], split[3]));
                    error += mod;
                    if (mod > 0) {
                        neverWon++;
                    }
                    stringBuilder.append(split[0]).append(',').append(0).append('\n');
                }
                test = tests.readLine();
            }
            System.out.println(error + " " + hadWonEarlier + " " + neverWon);
            System.out.println(csv.size());
            //System.out.println(stringBuilder.toString());
        }
    }

    private static int getResult(String s, String s1) {
        return s.equals("SO") ? (s1.equals("AC") ? 1 : 0) : 0;
    }

    private static int mod(int i) {
        return i < 0 ? -i : i;
    }
}
