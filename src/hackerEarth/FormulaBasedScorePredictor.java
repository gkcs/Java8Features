package hackerEarth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class FormulaBasedScorePredictor {

    public double getRating(final double lineNumber[], final double opponent_rating[], double result[]) {
        final double weightTieToDefinedRatingLevel = 2.5;
        final double weightOpponentRating = 12.5;
        final double gameWeight[] = new double[opponent_rating.length];
        for (int i = 0; i < opponent_rating.length; i++) {
            gameWeight[i] = Math.pow((1 / (1 + (100 - lineNumber[i]) / 48)), 2);
        }
        final double sumGameWeight = Arrays.stream(gameWeight).sum();
        double weighedPerformance = 0;
        double weighedOpponentRating = 0;
        final int extention = 850;
        for (int i = 0; i < opponent_rating.length; i++) {
            weighedPerformance += ((opponent_rating[i] + extention * (result[i] - 0.5)) * gameWeight[i]);
            weighedOpponentRating += weightOpponentRating * (opponent_rating[i] * gameWeight[i]) / sumGameWeight;
        }
        final double defined_rating_level = 1500;
        final double extraPoints = 24.5;
        final double weighedTieToDefinedRatingLevel = weightTieToDefinedRatingLevel * defined_rating_level;
        final double rating = (weighedPerformance + weighedOpponentRating + weighedTieToDefinedRatingLevel + extraPoints)
                / (sumGameWeight + weightOpponentRating + weightTieToDefinedRatingLevel);
        return rating;
    }

    public double getWinningProbabilty(double rating, double opponent_rating) {
        final int extention = 850;
        return (rating - opponent_rating) / extention + 0.5;
    }

    public static void main(String args[]) throws IOException {
        final HashMap<String, Double> players = new HashMap<>();
        String extension = "_with_min_value";
        //String extension = "";
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/player_ratings" + extension + ".csv"));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        while (s != null && !s.equals("")) {
            String[] split = s.split(",");
            players.put(split[0], Double.parseDouble(split[1]));
            s = bufferedReader.readLine();
        }
//        players.entrySet().stream().forEach(System.out::println);
        final RatingBasedPredictor ratingBasedPredictor = new RatingBasedPredictor();
        final BufferedReader test = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/test/test.csv"));
        test.readLine();
        s = test.readLine();
        int count = 0;
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Id,solved_status\n");
        HashMap<String, Double> initialRatings = getInitialRatings();
        while (s != null && !s.equals("")) {
            String[] split = s.split(",");
            String user = "U" + split[1];
            final boolean notFoundUser = players.get(user) == null;
            final Double firstPlayerRating = notFoundUser ? players.get("AVERAGE") : players.get(user);
            final String problem = "P" + split[2];
            boolean notFoundProblem = players.get(problem) == null;
            Double secondPlayerRating = notFoundProblem ? initialRatings.get(problem) : players.get(problem);
            if (!notFoundProblem || !notFoundUser) {
                count++;
            }
            double probabiltyOfSolving = ratingBasedPredictor.probabiltyOfSolving(firstPlayerRating, secondPlayerRating, 0);
            stringBuilder.append(split[0]).append(',').append(probabiltyOfSolving >= 0.5 ? 1 : 0).append('\n');
            s = test.readLine();
        }
        System.out.println(count);
        PrintWriter printWriter = new PrintWriter("/Users/gaurav.se/Documents/will_bill_solve_it/test_results.csv");
        printWriter.print(stringBuilder.toString());
        printWriter.close();
    }

    private static double getRating(int line, Double firstPlayerRating) {
        return (Math.pow((1d + line) / (1d + 35619), 1 / 3.5) * firstPlayerRating - 10) + 10;
    }

    private static Double getCorrespondingRating(String s) {
        switch (s) {
            case "E-M":
                return 12d;
            case "E":
                return 11d;
            case "M":
                return 13d;
            case "M-H":
                return 14d;
            case "H":
                return 15d;
            default:
                return 10d;
        }
    }

    public static HashMap<String, Double> getInitialRatings() throws IOException {
        final HashMap<String, Double> initialRatings = new HashMap<>();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/test/problems.csv"));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        while (s != null && !s.equals("")) {
            String[] split = s.split(",");
            initialRatings.put("P" + split[0], getCorrespondingRating(split[1]));
            s = bufferedReader.readLine();
        }
        return initialRatings;
    }
}
