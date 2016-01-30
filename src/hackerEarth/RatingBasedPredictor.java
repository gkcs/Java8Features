package hackerEarth;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//lowering lambda to 0.5 helped
public class RatingBasedPredictor {
    public static final double alpha = 0.1;
    private final double weightExponent = 2;
    private final double theta = 0.62;
    public final double maxIterationsForOptimization = 100;
    private final double lambda = 0.1;

    public double probabiltyOfSolving(final double firstPlayerRating, final double secondPlayerRating, final double skill) {
        if (secondPlayerRating - firstPlayerRating > 20) {
            return 0;
        } else if (secondPlayerRating - firstPlayerRating < -20) {
            return 1;
        }
        return 1 / (1 + Math.pow(2.718, secondPlayerRating - firstPlayerRating - skill));
    }

    public double weightOfProblem(final double currentTimestamp, final double factor, final double firstGameTimeStamp, final double lastGameTimeStamp) {
        return Math.pow((1 + (currentTimestamp - firstGameTimeStamp) * factor)
                / (1 + (lastGameTimeStamp - firstGameTimeStamp) * factor), weightExponent);
    }

    public double getLearningRate(final int currentIteration) {
        return Math.pow((alpha * maxIterationsForOptimization)
                / (currentIteration + alpha * maxIterationsForOptimization), theta);
    }

    public double getLoss(final double weights[],
                          final double outcomes[],
                          final double predictions[],
                          final double ratings[],
                          final double neighborRatings[]) {
        double loss = 0;
        for (int i = 0; i < outcomes.length; i++) {
            loss += weights[i] * (predictions[i] - outcomes[i]);
        }
        for (int i = 0; i < ratings.length; i++) {
            loss += lambda * Math.pow((ratings[i] - neighborRatings[i]), 2);
        }
        return loss;
    }

    public double getRatingDelta(final int currentIteration,
                                 final double weight,
                                 final double prediction,
                                 final double outcome,
                                 final int neighbors,
                                 final double rating,
                                 final double neighborRating) {
//        if (Double.isNaN(currentIteration) || Double.isInfinite(currentIteration)) {
//            throw new RuntimeException();
//        }
//        if (Double.isNaN(weight) || Double.isInfinite(weight)) {
//            throw new RuntimeException();
//        }
//        if (Double.isNaN(prediction) || Double.isInfinite(prediction)) {
//            throw new RuntimeException();
//        }
//        if (Double.isNaN(outcome) || Double.isInfinite(outcome)) {
//            throw new RuntimeException();
//        }
//        if (Double.isNaN(neighbors) || Double.isInfinite(neighbors)) {
//            throw new RuntimeException();
//        }
//        if (Double.isNaN(rating) || Double.isInfinite(rating)) {
//            throw new RuntimeException();
//        }
//        if (Double.isNaN(neighborRating) || Double.isInfinite(neighborRating)) {
//            throw new RuntimeException();
//        }
        //        if (Double.isNaN(delta)) {
//            throw new RuntimeException();
//        }
        return -getLearningRate(currentIteration)
                * (weight * (prediction - outcome) * prediction
                * (1 - prediction) +
                (lambda / neighbors) * (rating - neighborRating));
    }

}

class Predictor {

    private static final double timeSpan = 50;
    private static final RatingBasedPredictor ratingBasedPredictor = new RatingBasedPredictor();
    private static final double userGrowth = 1;
    private static final double problemGrowth = 1;

    public static void main(String args[]) throws IOException {
        final List<String[]> csv = new ArrayList<>();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/train/submissions.csv"));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        HashMap<Submission, Boolean> lines = new HashMap<>();
        while (s != null && !s.equals("")) {
            String[] split = s.split(",");
            split[0] = "U" + split[0];
            split[1] = "P" + split[1];
            Submission submission = new Submission(split[0], split[1]);
            if (lines.containsKey(submission)) {
                if (s.contains("SO") && s.contains("AC")) {
                    if (!lines.get(submission)) {
                        csv.add(split);
                    }
                }
            } else {
                csv.add(split);
            }
            lines.put(submission, s.contains("SO") && s.contains("AC"));
            s = bufferedReader.readLine();
        }
        System.out.println(csv.size());
        final double weights[] = getWeights(csv);
        final double outcomes[] = getOutcomes(csv);
        final double ratings[] = new double[getRatingSize(csv)];
        final HashMap<String, Integer> players = getPlayers(csv);
        final HashMap<String, Integer> initialRatings = getInitialRatings();
        final int[][][] neighbors = getNeighbors(players, csv);
        final int neighborhood[] = new int[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            neighborhood[i] = neighbors[i].length;
        }
        initialRatings.entrySet()
                .stream()
                .filter(initialRating -> players.containsKey(initialRating.getKey()))
                .forEach(initialRating -> ratings[players.get(initialRating.getKey())] = initialRating.getValue());
        final Submission submissions[] = getSubmissions(csv);
        final double ratingsWithMinimumLoss[] = new double[ratings.length];
        double minimumLoss = Integer.MAX_VALUE;
        for (int k = 0; k < ratingBasedPredictor.maxIterationsForOptimization; k++) {
            System.out.println("ITERATION: " + k);
            final double[] neighborRating = getNeighborRatings(weights, ratings, neighbors);
            final double[] predictions = getPredictions(submissions, ratings, players);
            for (int i = 0; i < submissions.length; i++) {
                Integer userID = players.get(submissions[i].userId);
                Integer problemID = players.get(submissions[i].problemId);
                ratings[userID] +=
                        userGrowth * ratingBasedPredictor.getRatingDelta(k,
                                weights[i],
                                predictions[i],
                                outcomes[i],
                                neighborhood[userID],
                                ratings[userID],
                                neighborRating[userID]) * getExperience(i, neighbors[userID][0][1]) * getForm(i, neighbors[userID][0][1]);
                ratings[problemID] += problemGrowth * ratingBasedPredictor.getRatingDelta(k,
                        -weights[i],
                        predictions[i],
                        outcomes[i],
                        neighborhood[problemID],
                        ratings[problemID],
                        neighborRating[problemID]);//* getNewness(i, neighbors[problemID][0][1]);
            }
            double loss = ratingBasedPredictor.getLoss(weights, outcomes, predictions, ratings, neighborRating);
            System.out.println(loss);
            if (loss < minimumLoss) {
                minimumLoss = loss;
                System.arraycopy(ratings, 0, ratingsWithMinimumLoss, 0, ratings.length);
            }
        }
        final double[] predictions = getPredictions(submissions, ratings, players);
        final double[] error = new double[predictions.length];
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("user_id")
                .append(',')
                .append("problem_id")
                .append(',')
                .append("solved_status")
                .append('\n');
        for (int i = 0; i < predictions.length; i++) {
            error[i] = mod(outcomes[i] - predictions[i]);
            stringBuilder.append(submissions[i].userId)
                    .append(',')
                    .append(submissions[i].problemId)
                    .append(',')
                    .append(predictions[i] >= 0.5 ? 1 : 0)
                    .append('\n');
        }
        printRatings(players, ratingsWithMinimumLoss, "_with_min_value");
        printRatings(players, ratings, "");
    }

    private static double getForm(int i, int i1) {
        return 1;
    }

    private static double getExperience(int current, Integer first) {
        return 1;
    }

    private static double getNewness(int current, double first) {
        return Math.pow(2, 1 / (current - first + 1));
    }

    private static void printRatings(HashMap<String, Integer> players, double[] ratingsWithMinimumLoss, String fileExtension) throws FileNotFoundException {
        final PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/Documents/will_bill_solve_it/player_ratings" + fileExtension + ".csv")));
        for (final Map.Entry<String, Integer> entry : players.entrySet()) {
            printWriter.println(entry.getKey() + "," + ratingsWithMinimumLoss[entry.getValue()]);
            printWriter.flush();
        }
        printWriter.println("AVERAGE" + ',' + Arrays.stream(ratingsWithMinimumLoss).average().getAsDouble());
        printWriter.close();
    }

    private static void printWithStatistics(double[] ratings, double[] ratingsWithMinimumLoss, double[] predictions, double[] error, StringBuilder stringBuilder) throws FileNotFoundException {
        final PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("/Users/gaurav.se/Documents/will_bill_solve_it/ratings_with_min_value.csv")));
        printWriter.println("RESULTS: ");
        printWriter.println(stringBuilder.toString());
        printWriter.flush();
        printWriter.println("STATS: ");
        printWriter.println(Arrays.stream(ratings).summaryStatistics());
        printWriter.flush();
        printWriter.println("RATINGS: ");
        printWriter.println(Arrays.toString(ratings));
        printWriter.flush();
        printWriter.println("PREDICTIONS: ");
        printWriter.println(Arrays.toString(predictions));
        printWriter.flush();
        printWriter.println("RATINGS WITH MINIMUM LOSS: ");
        printWriter.println(Arrays.toString(ratingsWithMinimumLoss));
        printWriter.flush();
        printWriter.println("ERROR: ");
        printWriter.println(Arrays.toString(error));
        printWriter.flush();
        printWriter.println("ERROR STATS: ");
        printWriter.println(Arrays.stream(error).summaryStatistics());
        printWriter.flush();
    }

    private static double mod(double v) {
        return v < 0 ? -v : v;
    }

    private static double[] getNeighborRatings(final double[] weights, final double[] ratings, final int[][][] neighbors) {
        final double neighborRating[] = new double[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            double neighborTotalWeight = 0;
            for (int j = 0; j < neighbors[i].length; j++) {
                neighborRating[i] += weights[neighbors[i][j][1]] * ratings[neighbors[i][j][0]];
                neighborTotalWeight += weights[neighbors[i][j][1]];
            }
            neighborRating[i] /= neighborTotalWeight;
        }
        return neighborRating;
    }

    //reducing the number of neighbors gave a shot up score

    private static int[][][] getNeighbors(final HashMap<String, Integer> players, final List<String[]> csv) {
        final HashMap<Integer, List<Integer>> neighbors = new HashMap<>();
        final HashMap<Integer, List<Integer>> neighborDates = new HashMap<>();
        for (int i = 0; i < csv.size(); i++) {
            String[] line = csv.get(i);
            if (!neighbors.containsKey(players.get(line[0]))) {
                neighbors.put(players.get(line[0]), new ArrayList<>());
                neighborDates.put(players.get(line[0]), new ArrayList<>());
            }
            if (!neighbors.containsKey(players.get(line[1]))) {
                neighbors.put(players.get(line[1]), new ArrayList<>());
                neighborDates.put(players.get(line[1]), new ArrayList<>());
            }
            neighbors.get(players.get(line[0])).add(players.get(line[1]));
            neighborDates.get(players.get(line[0])).add(i);
            neighbors.get(players.get(line[1])).add(players.get(line[0]));
            neighborDates.get(players.get(line[1])).add(i);
        }
        final boolean isProblem[] = new boolean[neighbors.size()];
        players.entrySet()
                .stream()
                .filter(entry -> entry.getKey().contains("P"))
                .forEach(entry -> isProblem[entry.getValue()] = true);
        final int neighborhood[][][] = new int[neighbors.size()][][];
        for (int i = 0; i < neighborhood.length; i++) {
            int start = 0, end = neighbors.get(i).size();
            if (isProblem[i]) {
                //end = (int) Math.ceil(end * 0.9);
            } else {
                //start = (int) Math.floor(end * 0.1);
            }
            neighborhood[i] = new int[end - start][2];
            final Integer[] nn = neighbors.get(i).toArray(new Integer[neighborhood[i].length]);
            final Integer[] dd = neighborDates.get(i).toArray(new Integer[neighborhood[i].length]);
            for (int j = 0; j < neighborhood[i].length; j++) {
                neighborhood[i][j][0] = nn[j + start];
                neighborhood[i][j][1] = dd[j + start];
            }
        }
        return neighborhood;
    }

    private static HashMap<String, Integer> getPlayers(final List<String[]> csv) {
        final HashMap<String, Integer> players = new HashMap<>();
        int count = 0;
        for (String a[] : csv) {
            if (!players.containsKey(a[0])) {
                players.put(a[0], count++);
            }
            if (!players.containsKey(a[1])) {
                players.put(a[1], count++);
            }
        }
        return players;
    }

    private static Submission[] getSubmissions(final List<String[]> csv) {
        final Submission[] submissions = new Submission[csv.size()];
        for (int i = 0; i < submissions.length; i++) {
            submissions[i] = new Submission(csv.get(i)[0], csv.get(i)[1]);
        }
        return submissions;
    }

    private static int getRatingSize(final List<String[]> csv) {
        final int users = csv.stream().map(a -> a[0]).collect(Collectors.toCollection(HashSet::new)).size();
        final int problems = csv.stream().map(a -> a[1]).collect(Collectors.toCollection(HashSet::new)).size();
        return users + problems;
    }

    public static double[] getWeights(final List<String[]> csv) {
        final int length = csv.size();
        final double factor = timeSpan / length;
        final double weights[] = new double[length];
        for (int i = 0; i < length; i++) {
            weights[i] = ratingBasedPredictor.weightOfProblem(i, factor, 0, length);
        }
        return weights;
    }

    public static double[] getOutcomes(final List<String[]> csv) {
        int length = csv.size();
        double outcomes[] = new double[length];
        for (int i = 0; i < length; i++) {
            outcomes[i] = csv.get(i)[2].equals("SO") ? (csv.get(i)[3].equals("AC") ? 1 : 0) : 0;
        }
        return outcomes;
    }

    public static double[] getPredictions(final Submission[] submissions,
                                          final double[] ratings,
                                          final HashMap<String, Integer> playerMap) {
        double predictions[] = new double[submissions.length];
        for (int i = 0; i < submissions.length; i++) {
            //// TODO: 17/01/16 Set skill level based on profile details
            predictions[i] = ratingBasedPredictor.probabiltyOfSolving(ratings[playerMap.get(submissions[i].userId)], ratings[playerMap.get(submissions[i].problemId)], 0);
        }
        return predictions;
    }

    public static HashMap<String, Integer> getInitialRatings() throws IOException {
        final HashMap<String, Integer> initialRatings = new HashMap<>();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaurav.se/Documents/will_bill_solve_it/train/problems.csv"));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        while (s != null && !s.equals("")) {
            String[] split = s.split(",");
            initialRatings.put("P" + split[0], getCorrespondingRating(split[1]));
            s = bufferedReader.readLine();
        }
        return initialRatings;
    }

    private static Integer getCorrespondingRating(String s) {
        switch (s) {
            case "E-M":
                return 12;
            case "E":
                return 11;
            case "M":
                return 13;
            case "M-H":
                return 14;
            case "H":
                return 15;
            default:
                return 10;
        }
    }
}