package hackerEarth;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ContestSolving {
    private static final String FOLDER = "/Users/gaurav.se/Documents/will_bill_solve_it/";
    private static final String PATH = FOLDER + "train/";

    public static void formatFile() throws IOException {
        final HashMap<String, String> userData = getData(PATH + "users.csv");
        final HashMap<String, String> problemData = getData(PATH + "problems.csv");
        final BufferedReader submissions = new BufferedReader(new FileReader(new File(PATH + "submissions.csv")));
        String submission = submissions.readLine();
        final StringBuilder stringBuilder = new StringBuilder();
        while (submission != null) {
            String split[] = submission.split(",");
            stringBuilder.append(userData.get(split[0])).append(',').append(problemData.get(split[1])).append(',');
            for (int i = 2; i < split.length; i++) {
                stringBuilder.append(split[i]).append(',');
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length()).append('\n');
            submission = submissions.readLine();
        }
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(PATH + "problem_data.csv"))));
        printWriter.print(stringBuilder.toString());
        submissions.close();
        printWriter.close();
        System.out.println("DONE");
    }

    //TRAINING USERS: 62531 TEST USERS: 15280
    //COMMON USERS: 6865
    //TRAINING PROBLEMS: 1003 TEST PROBLEMS: 957
    //COMMON PROBLEMS: 757

    public static void main(String args[]) throws IOException {
        HashSet<String> trainingUsers = findUsers(FOLDER + "train/problems.csv");
        HashSet<String> testUsers = findUsers(FOLDER + "test/problems.csv");
        System.out.println("TRAINING : " + trainingUsers.size() + " TEST : " + testUsers.size());
        System.out.println(trainingUsers.stream().filter(testUsers::contains).collect(Collectors.toList()).size());
        System.out.println(trainingUsers.stream().collect(Collectors.toList()));
    }

    public static HashSet<String> findUsers(String pathname) throws IOException {
        final HashSet<String> data = new HashSet<>();
        final BufferedReader lines = new BufferedReader(new FileReader(new File(pathname)));
        String line = lines.readLine();
        while (line != null && !line.equals("")) {
//            Submission submission = new Submission(line.split(",")[0], line.split(",")[1]);
//            System.out.println(submission);
            data.add(line.split(",")[1]);
            line = lines.readLine();
        }
        lines.close();
        return data;
    }

    private static HashMap<String, String> getData(String pathname) throws IOException {
        final HashMap<String, String> data = new HashMap<>();
        final BufferedReader lines = new BufferedReader(new FileReader(new File(pathname)));
        String line = lines.readLine();
        while (line != null && !line.equals("")) {
            data.put(line.split(",")[0], line.split(",")[1]);
            line = lines.readLine();
        }
        lines.close();
        return data;
    }
}

class Submission {
    @Override
    public String toString() {
        return "Submission{" +
                "userId='" + userId + '\'' +
                ", problemId='" + problemId + '\'' +
                '}';
    }

    public final String userId, problemId;

    Submission(String userId, String problemId) {
        this.userId = userId;
        this.problemId = problemId;
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || o instanceof Submission
                && userId.equals(((Submission) o).userId)
                && problemId.equals(((Submission) o).problemId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + problemId.hashCode();
        return result;
    }
}


